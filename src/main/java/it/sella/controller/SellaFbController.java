
package it.sella.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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

import it.sella.BotSession;
import it.sella.JsonUtil;
import it.sella.model.Entry;
import it.sella.model.Messaging;
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
	private static final String CHAT_URL="https://sella.it/sellabot/execute/user/chat";
    private static final String POLL_URL="https://sella.it/sellabot/execute/user/poll";

	private static final Logger logger = LoggerFactory.getLogger(SellaFbController.class);
	@GetMapping("/webhook")
	public ResponseEntity<?> verify(@RequestParam("hub.challenge") String challenge,
			@RequestParam("hub.verify_token") String token) {
		logger.info("<<<<<<<<<<<<<Challenge is:{} and token is {}>>>>>>>>>>>", challenge, token);
		if (token.equals("sellatoken123"))
			return new ResponseEntity<String>(challenge, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}
	
	@Autowired 
	private HttpSession httpSession;
	@PostMapping(path = "/webhook", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> getMessage(@RequestBody final String payLoad,
			@RequestHeader(SIGNATURE_HEADER_NAME) final String signature) {
		logger.info("<<<<<<<<<Response payload:{} && signature: {}>>>>>>>>>>", payLoad, signature);
		RequestPayload reqPayload=getResponseObject(payLoad);
		logger.info("<<<<<<<<<<<<<<<<reqpayload>>>>{}>>>>>>>>>>>>>",reqPayload);
		String eventType=getEventType(reqPayload);		
		for (Entry entry : reqPayload.getEntry()) {
			for (Messaging messaging : entry.getMessaging()) {
				final String textMessage = eventType.equals("PostbackEvent") ? messaging.getPostback().getPayload()	: messaging.getMessage().getText();
				logger.info("<<<<<<<<<<<<TextMessage::{},EventyType:::{}>>>>>>>>>>>>>>", textMessage, eventType);
				final String senderId = reqPayload.getEntry().get(0).getMessaging().get(0).getSender().getId();
				final String recipientId = reqPayload.getEntry().get(0).getMessaging().get(0).getRecipient().getId();
				logger.info("<<<<<<<<<<senderId>>>>{},RecipientId>>>{}>>>>>>>>>>>>>>>", senderId, recipientId);
				final UserDetail userDetail = getUserDetail(senderId);
				BotSession botSession=null;
				if(!httpSession.isNew()) {
					botSession = (BotSession) httpSession.getAttribute(recipientId);
					logger.info("<<<<<<<<<<<<<<<botsession available,HttpSessionId>>>>>>>>>>>>>>>>>",httpSession.getId());
				}else {
					logger.info("<<<<<<<<HttpSessionId::{}>>>>>",httpSession.getId());
					ResponseEntity<String> imLoginResponseEntity = imLogin(userDetail);
					if (imLoginResponseEntity.getStatusCode() != HttpStatus.FOUND) {
						logger.info("<<<<<<<<<<Login failed>>>>>>>>>");
					} else {
						logger.info("<<<<<<<<<<Loggedin successfully>>>>>>>>>");
						botSession = new BotSession();
						botSession.setFbReceipientId(recipientId);
						botSession.setFbSenderId(senderId);
						botSession.setImChatId(getChatId(imLoginResponseEntity.getHeaders()));
						botSession.setCokkieInfo(imLoginResponseEntity.getHeaders().getFirst("Set-Cookie"));
						httpSession.setAttribute(recipientId, botSession);						
					}
				}				
				logger.info("<<<<<<<<<<<<BotSession ::{}>>>>>>>>>>>>>", botSession);
				String senderActionAcknowledge = sendMessage(getSenderActionResonse("mark_seen", senderId));
				logger.info("<<<<<<<<<<senderActionAcknowledge::::{}>>>>>>>>>>>>", senderActionAcknowledge);
				senderActionAcknowledge = sendMessage(getSenderActionResonse("typing_on", senderId));
				logger.info("<<<<<<<<<<<<<senderActionAcknowledge::::{}>>>>>>>>>>>>>>", senderActionAcknowledge);
				sendImMessage(botSession.getImChatId(), textMessage, botSession.getCokkieInfo());
				getPollResponse(botSession.getFbSenderId(), botSession.getImChatId(), botSession.getCokkieInfo(), 10);
				// sendMessage(QnaResponse.getJsonResponse(senderId,textMessage!=null?textMessage.toLowerCase():"",userDetail));
				senderActionAcknowledge = sendMessage(getSenderActionResonse("typing_off", senderId));
				logger.info("senderActionAcknowledge>>>>{}", senderActionAcknowledge);
			}
		}		
		return new ResponseEntity<String>("Success", HttpStatus.OK);
	}
	
	public String sendMessage(String payLoad) {
		String url = String.format(FB_GRAPH_API_URL_MESSAGES, ACCESS_TOKEN);
		logger.info("<<<<<<<<FB_GRAPH_API_URL_MESSAGES:::::::{}>>>>>>>>>>>",url);
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
		logger.info("IM chat init url>>>>{}", url);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> indexHtml = restTemplate.getForEntity(url, String.class);

		logger.info("The HttpsLoginStatus {}", indexHtml.getStatusCode());
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
		logger.info("ChatId:::{}", newChatInfo.getChatid());

		MessagePayload messagepayload = new MessagePayload();
		messagepayload.setAction("chatevent");
		messagepayload.setIdevent("chatmessage");
		messagepayload.setChatid(newChatInfo.getChatid());
		Eventdatum eventdatum = new Eventdatum();
		eventdatum.setName("message");
		eventdatum.setValue(textMessage);
		messagepayload.addEventDatum(eventdatum);
		HttpEntity<MessagePayload> messageEntity = new HttpEntity<>(messagepayload, headers);
		logger.info("messagePayload::{}", messagepayload.toString());
		restTemplate.postForEntity(chatUrl, messageEntity, String.class);

		String pollUrl = "https://sella.it/sellabot/execute/user/poll";
		String pollPayload = String.format("{\"chatid\":\"%s\"}", newChatInfo.getChatid());
		logger.info("pollpayload::{}", pollPayload);

		HttpEntity<String> pollEntity = new HttpEntity<>(pollPayload, headers);
		for (int i = 0; i < 16; i++) {
			logger.info("poll count{}", i + 1);
			PollResponse pollResponse = restTemplate.postForEntity(pollUrl, pollEntity, PollResponse.class).getBody();
			logger.info("pollresponse count:::{}", pollResponse.getResults().size());
			StringBuilder responseAnswer = new StringBuilder();
			StringBuilder responseMessage = new StringBuilder();
			String link = null;
			StringBuilder responseString = new StringBuilder();
			for (Result result : pollResponse.getResults()) {
				logger.info("Result:::{}", result);
				final String answer = result.getAnswer();
				final String message = result.getMessage();
				if (answer != null || message != null) {
					responseString.append(answer != null ? answer : message);
					String imResponse = String.format(
							"{ \"recipient\": { \"id\": \"%s\" }, \"message\": { \"text\": \"%s\" } }", receipientId,
							responseString);
					sendMessage(imResponse);
					if (result.getLink() != null) {
						imResponse = String.format(
								"{ \"recipient\":{ \"id\":\"%s\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"open_graph\", \"elements\":[ { \"url\":\"%s\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it\", \"title\":\"View More\" } ] } ] } } } }",
								receipientId, result.getLink());
						sendMessage(imResponse);
					}
				}
			}
			logger.info("response answer11:::{}", responseAnswer);
			logger.info("response message11:::{}", responseMessage);
			logger.info("response link:::{}", link);
		}
	}
	
	private ResponseEntity<String> imLogin(final UserDetail userDetail){
		String url = String.format(IM_LOGIN_URL, userDetail.getFirstName(), userDetail.getLastName());
		logger.info("IM chat init url>>>>{}", url);		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> imLoginResponseEntity = restTemplate.getForEntity(url, String.class);
		return imLoginResponseEntity;
	}
	
	
	private String getChatId(HttpHeaders headers) {		
		final String cookieInfo = headers.getFirst("Set-Cookie");
		String newChatPayload = "{\"action\":\"newchat\",\"sourceIntentCode\":\"\"}";
		final RestTemplate restTemplate = new RestTemplate();
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Cookie", cookieInfo);
		final HttpEntity<String> chatEntity = new HttpEntity<>(newChatPayload, headers);
		final NewChatInfo newChatInfo = restTemplate.postForEntity(CHAT_URL, chatEntity, NewChatInfo.class).getBody();
		logger.info("<<<<<<<<ChatId:::{}>>>>>>>",newChatInfo.getChatid());
		return newChatInfo.getChatid();
	}
	
	private ResponseEntity<String> sendImMessage(final String chatId, final String fbMessage, final String cookieInfo) {
		final MessagePayload messagepayload = new MessagePayload();
		messagepayload.setAction("chatevent");
		messagepayload.setIdevent("chatmessage");
		messagepayload.setChatid(chatId);
		final Eventdatum eventdatum = new Eventdatum();
		eventdatum.setName("message");
		eventdatum.setValue(fbMessage);
		messagepayload.addEventDatum(eventdatum);
		logger.info("<<<<<<<<<<<<<<<<messagePayload::{}>>>>>>>>>>>",messagepayload);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Cookie",cookieInfo);
		final HttpEntity<MessagePayload> messageEntity = new HttpEntity<>(messagepayload, headers);
		final RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> sendMessageResponseEntity = restTemplate.postForEntity(CHAT_URL, messageEntity, String.class);
		return sendMessageResponseEntity;
	}
	
	private void getPollResponse(final String recipientId, final String chatId, final String cookieInfo,int totalPolls) {
		String pollPayload = String.format("{\"chatid\":\"%s\"}", chatId);
		logger.info("<<<<<<<<<pollpayload::{}>>>>>>>>>>>>>>>",pollPayload);
		final RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Cookie",cookieInfo);
		HttpEntity<String> pollEntity = new HttpEntity<>(pollPayload, headers);
		for(int i=0;i<totalPolls;i++) {	
			logger.info("<<<<<<<<<<poll No.{}>>>>>>>>>>>",(i+1));
			PollResponse pollResponse = restTemplate.postForEntity(POLL_URL, pollEntity, PollResponse.class).getBody();
			logger.info("<<<<<<<<<<<<Total Poll Results :::{}>>>>>>>>>>>",pollResponse.getResults().size());
			StringBuilder responseString= new StringBuilder();
			for(Result result:pollResponse.getResults()) {
				logger.info("<<<<<<<<<<<<Each Result :::{}>>>>>>>>>>>>>",result);
				final String answer = result.getAnswer();
				final String message = result.getMessage();	
				String defaultMessage="Sono Stella, assistente virtuale di Banca Sella. Come ti posso aiutare?";
				if(answer!=null || (message!=null && !message.equals(defaultMessage))) {
					responseString.append(answer!=null?answer:message);
					String imResponse = String.format("{ \"recipient\": { \"id\": \"%s\" }, \"message\": { \"text\": \"%s\" } }",recipientId,responseString);
					sendMessage(imResponse);
					if(result.getLink()!=null) {
						imResponse = String.format("{ \"recipient\":{ \"id\":\"%s\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"open_graph\", \"elements\":[ { \"url\":\"%s\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it\", \"title\":\"View More\" } ] } ] } } } }",recipientId, result.getLink());
						sendMessage(imResponse);
					}
				}
			}	
		}
	}	
	
}
