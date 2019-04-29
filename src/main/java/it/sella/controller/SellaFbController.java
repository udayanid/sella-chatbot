
package it.sella.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import it.sella.IMSession;
import it.sella.JsonUtil;
import it.sella.QnaResponse;
import it.sella.model.Entry;
import it.sella.model.Messaging;
import it.sella.model.RequestPayload;
import it.sella.model.UserDetail;
import it.sella.model.im.ChatResponse;

@RestController
public class SellaFbController {
	private static final Logger logger = LoggerFactory.getLogger(SellaFbController.class);
	private static final String SIGNATURE_HEADER_NAME = "X-Hub-Signature";
	private static final String ACCESS_TOKEN = "EAAUD51fNmIEBAGNJi2gFBTOlNdZCZBoREqto8BqQHlzqdSCJAkDUQF6uaV1XCZBSSLJj5Km2smraSFZCUKx1bhAZAsjIxfnqYGhCX7J39IOJMgmEFHZCGgvA094DdzoCZCgkU6LiivUwZAZB3xZCROBsLxDBXg2OVINYOJaewtGzrbx9e36KCkKbbr";
	private static final String FB_GRAPH_API_URL_MESSAGES = "https://graph.facebook.com/v2.6/me/messages?access_token=%s";
	private static Map<String, IMSession> chatSessionMap = new HashMap<String, IMSession>();


	@GetMapping("/webhook")
	public ResponseEntity<?> verify(@RequestParam("hub.challenge") String challenge,
			@RequestParam("hub.verify_token") String token) {
		logger.info("<<<<<<<<<<<<<Hub Challenge is:{} and token is {}>>>>>>>>>>>>>", challenge, token);
		if (token.equals("sellatoken123"))
			return new ResponseEntity<String>(challenge, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
	}

	@PostMapping(path = "/webhook", consumes = "application/json", produces = "application/json")
	public ResponseEntity<?> getMessage(@RequestBody final String payLoad,	@RequestHeader(SIGNATURE_HEADER_NAME) final String signature) {
		logger.info("<<<<<<<<<FB Request payload:{} && FB signature: {}>>>>>>>>>>", payLoad, signature);
		
		final RequestPayload requestPayload = getRequestObject(payLoad, RequestPayload.class);
		logger.info("--------------------------------------------------------------------------------------------------------------");
		logger.info("<<<<<<<<<<<<<<<<reqpayload>>>>{}>>>>>>>>>>>>>", requestPayload);
		logger.info("--------------------------------------------------------------------------------------------------------------");
		
		final String senderId = getSenderId(requestPayload);
		final String recipientId = getReceipientId(requestPayload);
		logger.info("<<<<<<<<<<senderId>>>>{}-------RecipientId>>>{}>>>>>>>>>>>>>>>", senderId, recipientId);
		
		final UserDetail userDetail = getUserDetail(senderId);
		logger.info("<<<<<<<<<<<<<<<<<<<UserDetil:{}>>>>>>>>>>>>>",userDetail);
		
		
		String eventType = getEventType(requestPayload);
		logger.info("<<<<<<<<<The requested event Type {}>>>>>>>>>>>>>", eventType);	
		
		// Iterating each facebook message entry  and sending it to the bot server
		int total_msg = 0;
		for (Entry entry : requestPayload.getEntry()) {
			
			total_msg++;
			logger.info("<<<<<<<<<<<<Total facebook message entry ::{}>>>>>>>>>>>>>>", total_msg);
			
			for (Messaging messaging : entry.getMessaging()) {
				final String fbMessage = eventType.equals("PostbackEvent") ? messaging.getPostback().getPayload() : messaging.getMessage().getText();
				logger.info("<<<<<<<<<<<<TextMessage::{},EventyType:::{}>>>>>>>>>>>>>>", fbMessage, eventType);
				String senderActionAcknowledge = sendFBMessage(getSenderActionResonsePayload("mark_seen", senderId));
				IMSession imSession=getUserSession(recipientId, senderId, userDetail);
				logger.info("<<<<<<<<<<<<<imSession::{}>>>>>>>>>>>>",imSession);
				imProcess(imSession, fbMessage);
				senderActionAcknowledge = sendFBMessage(getSenderActionResonsePayload("typing_off", senderId));
				logger.info("senderActionAcknowledge>>>>{}>>>>>>>>>>>", senderActionAcknowledge);				
			}
		}
		return new ResponseEntity<String>("Success", HttpStatus.OK);
	}
	

	
	
	/**Method to send fb message to messenger 
	 * @param fbResponsePayload :String
	 * @return
	 */
	public static String sendFBMessage(String fbResponsePayload) {
		logger.info("<<<<<<<<<<<<<<<<<<<ResponsePayload::{}>>>>>>>>>>>>",fbResponsePayload);
		final String url = String.format(FB_GRAPH_API_URL_MESSAGES, ACCESS_TOKEN);
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		final HttpEntity<String> entity = new HttpEntity<String>(fbResponsePayload, headers);
		return restTemplate.postForObject(url, entity, String.class); 
	}

	
	/**method to get user detail : UserDetail
	 * @param senderId:String
	 * @return 
	 */
	public UserDetail getUserDetail(String senderId) {
		final String formattedUrl = "https://graph.facebook.com/%saccess_token=%s";
		final String url = String.format(formattedUrl, senderId + "?fields=first_name,last_name,profile_pic&", ACCESS_TOKEN);
		final RestTemplate restTemplate = new RestTemplate();
		final UserDetail userDetail = restTemplate.getForObject(url, UserDetail.class);
		return userDetail;
	}

	/**Method to get SenderAction ResonsePayload 
	 * @param senderAction : String
	 * @param senderId : String
	 * @return String 
	 */
	private String getSenderActionResonsePayload(final String senderAction, final String senderId) {		
		return String.format("{ \"recipient\":{ \"id\":\"%s\" }, \"sender_action\":\"%s\" }", senderId, senderAction);
	}

	
	/**Method to get ResponseObject as  RequestPayload
	 * @param responsePayload : String
	 * @return requestPayload: RequestPayload
	 */	
	public static <T> T getRequestObject(final String responsePayload,Class<T> clazz) {		
		return JsonUtil.getInstance().fromJson(responsePayload, clazz);
	}

	
	/**Method to get event type as String
	 * @param requestPayload : String
	 * @return eventType : String
	 */
	private String getEventType(RequestPayload requestPayload) {
		String eventType = "TextEvent";
		if (requestPayload.getEntry().get(0).getMessaging().get(0).getPostback() != null) {
			eventType = "PostbackEvent";
		}		
		return eventType;
	}	
	
	/*
	 * Facebook RequestPayload:-
	 * {
	"object": "page",
	"entry": [{
		"id": "437062153490261",
		"time": 1539616619429,
		"messaging": [{
			"sender": {
				"id": "1841499292614128"
			},
			"recipient": {
				"id": "437062153490261"
			},
			"timestamp": 1539616618858,
			"message": {
				"mid": "F0LZKLRf0MQRHGQ6dYWTT4e0xl-rcOSVgGN5z_iUHtiBdMDf3S8XzLzrnz-rruC5Op_r4Bg2sBUpZb0_yGPGIw",
				"seq": 127,
				"text": "hi"
			}
		}]
	}]
}*/
	
	/**Method to get senderId from the fb request payload
	 * @param requestPayload
	 * @return
	 */
	private String getSenderId(final RequestPayload requestPayload) {
		return requestPayload.getEntry().get(0).getMessaging().get(0).getSender().getId();
	}
	
	/**Method to get receipientId from the fb request payload
	 * @param requestPayload
	 * @return
	 */
	private String getReceipientId(final RequestPayload requestPayload) {
		return requestPayload.getEntry().get(0).getMessaging().get(0).getRecipient().getId();
	}
	
	/**Method to get chat session
	 * @param recipientId:String
	 * @param senderId :String
	 * @param userDetail :UserDetail
	 * @return
	 */
	private IMSession getUserSession(final String recipientId, final String senderId, final UserDetail userDetail) {
		IMSession imSession = null;
		if (chatSessionMap.containsKey(recipientId)) {
			logger.info("<<<<<<<<<<<<<<<ImSession already available in session>>>>>>>>>>>>>>>>>");
			imSession = (IMSession) chatSessionMap.get(recipientId);
			if (LocalDateTime.now().isAfter(imSession.getLastRequestDate().plusMinutes(25))) {
				imSession = IMBotClient.getNewBotSession(userDetail, senderId, recipientId);
			}else if(LocalDateTime.now().isAfter(imSession.getLastRequestDate().plusMinutes(2))) {
				imSession.setImChatId(IMBotClient.getNewChatId(imSession.getCookieInfo()));
				imSession.setLastRequestDate(LocalDateTime.now());
			}
		} else {
			imSession = IMBotClient.getNewBotSession(userDetail, senderId, recipientId);
		}
		chatSessionMap.put(recipientId, imSession);
		return imSession;		
	}
	
	/**Method to process Im Request/Response through messenger Interface
	 * @param imSession:IMSession
	 * @param fbMessage:String
	 */
	private void imProcess(final IMSession imSession, final String fbMessage) {
		final ChatResponse chatResponse =IMBotClient.sendImMessage(imSession, fbMessage).getBody();
		imSession.setLastRequestDate(LocalDateTime.now());		
		logger.info("<<<<<<<<<ChatResponse:::{}>>>>>>>>>>>>>>>", chatResponse);
		if(chatResponse.getStatus().equals("EXCEPTION")) {
			imSession.setImChatId(IMBotClient.getNewChatId(imSession.getCookieInfo()));
			imSession.setLastRequestDate(LocalDateTime.now());
			imProcess(imSession, fbMessage);
		}
		IMBotClient.getPollResponse(imSession, 8);
	}
	
}
