
package it.sella.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import it.sella.JsonUtil;
import it.sella.model.RequestPayload;
import it.sella.model.UserDetail;
import it.sella.model.im.Eventdatum;
import it.sella.model.im.MessagePayload;
import it.sella.model.im.NewChatInfo;
import it.sella.model.im.PollResponse;
import it.sella.model.im.Result;

@RestController
public class SellaFbController {
    //https://sella.it/sellabot/chatinit?nome=nome1&cognome=cognome1&email=test@sella.it&CHANNEL=Sella_sito_free
	private static final String SIGNATURE_HEADER_NAME = "X-Hub-Signature";
	private static final String ACCESS_TOKEN = "EAADwyglYT3gBACIGJ5VroCVMAiZAtbW2zsKihP6iClcAeCDrPgusQQNuI6jPvEshBF0TgwW2CzVRIQZCf5ZC6uYe8CXMTY8cnat4OfBgJzsWZAZCRDWaw9N29ZCsy2KZCcCS5mvRmooIkbB3TclHrJIAZAah0SPJTVJ2g2ZB9fExG9w0QmPyWRyQR";
	private static final String FB_GRAPH_API_URL_MESSAGES = "https://graph.facebook.com/v2.6/me/messages?access_token=%s";
	private static final String IM_LOGIN_URL = "https://sella.it/sellabot/chatinit?nome=%s&cognome=%s&email=test3@sella.it&CHANNEL=Sella_sito_free";

	private static final Logger logger = LoggerFactory.getLogger(SellaFbController.class);

	@GetMapping("/webhook")
	public ResponseEntity<?> verify(@RequestParam("hub.challenge") String challenge,
			@RequestParam("hub.verify_token") String token) {
		logger.info("Challenge is:{} and token is {}", challenge, token);
		if (token.equals("sellatoken123"))
			return new ResponseEntity<String>(challenge, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@PostMapping(path = "/webhook", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> getMessage(@RequestBody final String payLoad,
			@RequestHeader(SIGNATURE_HEADER_NAME) final String signature) {
		logger.info("Response payload:{} && signature: {}", payLoad, signature);
		RequestPayload reqPayload=getResponseObject(payLoad);
		logger.info("reqpayload>>>>{}",reqPayload);
		String eventType=getEventType(reqPayload);
		String textMessage="";
		if(eventType.equals("PostbackEvent")) {
			textMessage=reqPayload.getEntry().get(0).getMessaging().get(0).getPostback().getPayload();
			logger.info("getPayload>>>>{}",textMessage);
		}else{
			 textMessage = reqPayload.getEntry().get(0).getMessaging().get(0).getMessage().getText();
			 logger.info("getRequestedText>>>>{}",textMessage);
		}
		String senderId= reqPayload.getEntry().get(0).getMessaging().get(0).getSender().getId();
		logger.info("senderId>>>>{}",senderId);
		String senderActionAcknowledge = sendMessage(getSenderActionResonse("mark_seen", senderId));
		logger.info("senderActionAcknowledge>>>>{}",senderActionAcknowledge);
		senderActionAcknowledge = sendMessage(getSenderActionResonse("typing_on", senderId));
		logger.info("senderActionAcknowledge>>>>{}",senderActionAcknowledge);
		UserDetail userDetail=getUserDetail(senderId);
		sendMessageFromIM(senderId, textMessage, userDetail);
		//sendMessage(QnaResponse.getJsonResponse(senderId, textMessage!=null?textMessage.toLowerCase():"",userDetail));
	    senderActionAcknowledge = sendMessage(getSenderActionResonse("typing_off", senderId));
	    logger.info("senderActionAcknowledge>>>>{}",senderActionAcknowledge);
		return new ResponseEntity<String>("Success", HttpStatus.OK);
	}
	
	public String sendMessage(String payLoad) {
		String url = String.format(FB_GRAPH_API_URL_MESSAGES, ACCESS_TOKEN);
		logger.info("url>>>>{}",url);
		RestTemplate restTemplate=new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);	
		HttpEntity<String> entity = new HttpEntity<String>(payLoad, headers);		
		return restTemplate.postForObject(url, entity, String.class);
	}
	
	public UserDetail getUserDetail(String senderId) {
		String formattedUrl = "https://graph.facebook.com/%saccess_token=%s";
		String url = String.format(formattedUrl, senderId + "?fields=first_name,last_name,profile_pic&", ACCESS_TOKEN);
		RestTemplate restTemplate=new RestTemplate();	
		UserDetail userDetail = restTemplate.getForObject(url, UserDetail.class);
		return userDetail;
	}
	
	
	
	private String getSenderActionResonse(final String senderAction, final String senderId) {
		return String.format("{ \"recipient\":{ \"id\":\"%s\" }, \"sender_action\":\"%s\" }", senderId,senderAction);
	}	
	

	//to get the json to gson object
	private RequestPayload getResponseObject(final String responsePayload) {		
		return JsonUtil.getInstance().fromJson(responsePayload, RequestPayload.class);
	}
	
	//to get the requested eventype
	private String getEventType(RequestPayload requestPayload) {
		String eventType="TextEvent";
		if(requestPayload.getEntry().get(0).getMessaging().get(0).getPostback()!=null) {
			eventType="PostbackEvent";
		}
		logger.info("The requested event Tyepe {}",eventType);
		return eventType;		
	}
	
	public void sendMessageFromIM(String receipientId, String textMessage, UserDetail userDetail) {
		String url = String.format(IM_LOGIN_URL, userDetail.getFirstName(), userDetail.getLastName());
		logger.info("IM url>>>>{}", url);		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> indexHtml = restTemplate.getForEntity(url, String.class);
	    logger.info("The HttpsLoginStatus {}",indexHtml.getStatusCode());
		if (indexHtml.getStatusCode() != HttpStatus.FOUND) {
			logger.info("The HttpsLoginStatus::: failed");
		}		
		HttpHeaders headers = indexHtml.getHeaders();
		String cookieInfo = headers.getFirst("Set-Cookie");
		final String chatUrl = "https://sella.it/sellabot/execute/user/chat";
		String newChatPayload = "{\"action\":\"newchat\",\"sourceIntentCode\":\"\"}";
		restTemplate = new RestTemplate();
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Cookie", cookieInfo);

		HttpEntity<String> chatEntity = new HttpEntity<>(newChatPayload, headers);
		NewChatInfo newChatInfo = restTemplate.postForEntity(chatUrl, chatEntity, NewChatInfo.class).getBody();
		logger.info("ChatId:::{}",newChatInfo.getChatid());

		MessagePayload messagepayload = new MessagePayload();
		messagepayload.setAction("chatevent");
		messagepayload.setIdevent("chatmessage");
		messagepayload.setChatid(newChatInfo.getChatid());
		Eventdatum eventdatum = new Eventdatum();
		eventdatum.setName("message");
		eventdatum.setValue(textMessage);
		messagepayload.addEventDatum(eventdatum);
		HttpEntity<MessagePayload> messageEntity = new HttpEntity<>(messagepayload, headers);
		logger.info("messagePayload::{}",messagepayload.toString());
		restTemplate.postForEntity(chatUrl, messageEntity, String.class);

		String pollUrl = "https://sella.it/sellabot/execute/user/poll";
		String pollPayload = String.format("{\"chatid\":\"%s\"}", newChatInfo.getChatid());
		logger.info("pllpayload::{}",pollPayload);

		HttpEntity<String> pollEntity = new HttpEntity<>(pollPayload, headers);

//		for (int i = 0; i < 16; i++) {
//			logger.info("poll count{}",i+1);
//			PollResponse pollResponse = restTemplate.postForEntity(pollUrl, pollEntity, PollResponse.class).getBody();
//			int count=0;
//			if (pollResponse.getResults().size() > 0) {
//				count++;
//				Result result = pollResponse.getResults().get(0);
//				String answer = result.getAnswer();
//				logger.info("ResultArray{} AND COUNT{}",result,count++);
//				logger.info("Answer:::{}",answer);
//				if (answer != null) {
//					String imResponse = String.format("{ \"recipient\": { \"id\": \"%s\" }, \"message\": { \"text\": \"%s\" } }",receipientId,answer);
//					sendMessage(imResponse);
//					if(result.getLink()!=null) {
//						imResponse=String.format("{ \"recipient\":{ \"id\":\"%s\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"open_graph\", \"elements\":[ { \"url\":\"%s\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it\", \"title\":\"View More\" } ] } ] } } } }",receipientId,result.getLink());
//						sendMessage(imResponse);
//					}
//				}
//			}
//		}
		for(int i=0;i<8;i++) {	
			logger.info("poll count{}",i+1);
			PollResponse pollResponse = restTemplate.postForEntity(pollUrl, pollEntity, PollResponse.class).getBody();
			logger.info("pollresponse count:::{}",pollResponse.getResults().size());
			StringBuilder responseAnswer=new StringBuilder();
			StringBuilder responseMessage=new StringBuilder();
			String link=null;
			for(Result result:pollResponse.getResults()) {
				logger.info("Result:::{}",result);
				final String answer = result.getAnswer();
				final String message = result.getMessage();	
				if(answer!=null) {
					responseAnswer.append(answer);
					if(result.getLink()!=null) {
						link=result.getLink();
					}
				}else if(message!=null) {
					responseMessage.append(message).append(", ");
				}
			}	
			logger.info("response answer11:::{}",responseAnswer);
			logger.info("response message11:::{}",responseMessage);
			logger.info("response link:::{}",link);

			if(!responseAnswer.toString() .equals(""))	{
				logger.info("response answer:::{}",responseAnswer);
				String imResponse = String.format("{ \"recipient\": { \"id\": \"%s\" }, \"message\": { \"text\": \"%s\" } }",receipientId,responseAnswer);
				sendMessage(imResponse);
				if(link!=null) {
					imResponse=String.format("{ \"recipient\":{ \"id\":\"%s\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"open_graph\", \"elements\":[ { \"url\":\"%s\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it\", \"title\":\"View More\" } ] } ] } } } }",receipientId,link);
					sendMessage(imResponse);
				}
			 }
			if(!responseMessage.toString().equals("")) {
				logger.info("response message:::{}",responseMessage);
				String imResponse = String.format("{ \"recipient\": { \"id\": \"%s\" }, \"message\": { \"text\": \"%s\" } }",receipientId,responseMessage);
				sendMessage(imResponse);
			}
			
		}
	}	
	
}
