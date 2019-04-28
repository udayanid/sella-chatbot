package it.sella.controller;

import java.time.LocalDateTime;

import org.hibernate.engine.spi.SessionDelegatorBaseImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import it.sella.IMSession;
import it.sella.model.UserDetail;
import it.sella.model.im.ChatResponse;
import it.sella.model.im.Eventdatum;
import it.sella.model.im.MessagePayload;
import it.sella.model.im.NewChatInfo;
import it.sella.model.im.PollResponse;
import it.sella.model.im.Result;

public class IMBotClient
{	
	private static final Logger logger = LoggerFactory.getLogger(IMBotClient.class);
	private static final String IM_LOGIN_URL = "https://sella.it/sellabot/chatinit?nome=%s&cognome=%s&email=%s&CHANNEL=Sella_sito_free";
	private static final String CHAT_URL = "https://sella.it/sellabot/execute/user/chat";
	private static final String POLL_URL = "https://sella.it/sellabot/execute/user/poll";

	/**method to do im login
	 * @param userDetail
	 * @return
	 */
	private static ResponseEntity<String> doIMLogin(final UserDetail userDetail) {
		final String mailId = userDetail.getFirstName().concat("_").concat(userDetail.getLastName()).concat("@test.it");
		final String url = String.format(IM_LOGIN_URL, userDetail.getFirstName(), userDetail.getLastName(), mailId);
		logger.info("<<<<<<<doIMLogin::: {} >>>>>>>>>>>>>", url);
		final RestTemplate restTemplate = new RestTemplate();
		final ResponseEntity<String> imLoginResponseEntity = restTemplate.getForEntity(url, String.class);
		return imLoginResponseEntity;
	}
	
	
	
	/**Method to get New IM chat id as String
	 * @param cookieInfo
	 * @return 
	 */
	public static String getNewChatId(String cookieInfo) {
		final String newChatRequetPayload = "{\"action\":\"newchat\",\"sourceIntentCode\":\"\"}";
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Cookie", cookieInfo);
		final HttpEntity<String> chatEntity = new HttpEntity<>(newChatRequetPayload, headers);
		final NewChatInfo newChatInfo = restTemplate.postForEntity(CHAT_URL, chatEntity, NewChatInfo.class).getBody();
		logger.info("<<<<<<<<getNewChatId:::{}>>>>>>>", newChatInfo.getChatid());
		return newChatInfo.getChatid();
	}
	
	
	/**Method to get new IM chat session
	 * @param userDetail
	 * @param senderId
	 * @param recepientId
	 * @return
	 */
	public static IMSession getNewBotSession(final UserDetail userDetail, final String senderId, final String recepientId) {
		final ResponseEntity<String> imLoginResponseEntity = doIMLogin(userDetail);
		IMSession imSession = null;
		logger.info("<<<<<<<<<<<<<<<<<<Login status code:::{}>>>>>>>>>>>", imLoginResponseEntity.getStatusCode());
		if (imLoginResponseEntity.getStatusCode() != HttpStatus.FOUND) {
			logger.info("<<<<<<<<<<Login KO>>>>>>>>>");
		} else {
			logger.info("<<<<<<<<<<Login OK>>>>>>>>>");
			imSession = new IMSession();
			imSession.setFbReceipientId(recepientId);
			imSession.setFbSenderId(senderId);
			final HttpHeaders headers = imLoginResponseEntity.getHeaders();
			final String cookieString = headers.getFirst("Set-Cookie");
			imSession.setImChatId(getNewChatId(cookieString));
			imSession.setCookieInfo(cookieString);
			imSession.setLastRequestDate(LocalDateTime.now());
		}
		return imSession;
	}
	
	/**Method to send fb message to IM
	 * @param chatId
	 * @param fbMessage
	 * @param cookieInfo
	 * @return
	 */
	public static ResponseEntity<ChatResponse> sendImMessage(final IMSession imSession, final String fbMessage) {
		final MessagePayload messagepayload = new MessagePayload();
		messagepayload.setAction("chatevent");
		messagepayload.setIdevent("chatmessage");
		messagepayload.setChatid(imSession.getImChatId());
		final Eventdatum eventdatum = new Eventdatum();
		eventdatum.setName("message");
		eventdatum.setValue(fbMessage);
		messagepayload.addEventDatum(eventdatum);
		logger.info("<<<<<<<<<<<<<<<<IM requestMessagePayload::{}>>>>>>>>>>>", messagepayload);
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Cookie", imSession.getCookieInfo());
		final HttpEntity<MessagePayload> messageEntity = new HttpEntity<>(messagepayload, headers);
		final RestTemplate restTemplate = new RestTemplate();
		final ResponseEntity<ChatResponse> sendImMessageResponseEntity = restTemplate.postForEntity(CHAT_URL, messageEntity, ChatResponse.class);
		logger.info("<<<<<<<<<<<sendImMessageResponseEntity:::{}>>>>>>>>>>>>>>>>",sendImMessageResponseEntity.getStatusCode());
		return sendImMessageResponseEntity;
		/*
		 *
		 * Requesting to bot for new message { "action": "chatevent", "chatid":
		 * "d2bj9hvl1dp0fe392hls908e51", "idevent": "chatmessage", "sourceIntentCode":
		 * "", "eventdata": [{ "name": "message", "value": "hello" }] }
		 * 
		 * incase of chatid not found in bot { "status": "EXCEPTION", "errors": [{
		 * "messageCode": "IM_CHAT_ID_NOT_FOUND", "messageParams": [],
		 * "messageFEFields": "*" }], "requests": null, "codes": null, "contextChange":
		 * null, "concepts": null, "favorites": null, "chatid": null, "chaturl": null,
		 * "result": null, "cause": null, "licenses": null, "transcript": null,
		 * "overTime": null, "errorMessageCode": "IM_CHAT_ID_NOT_FOUND" }
		 *
		 */
	}
	

	/**Method to get IM Polling Response
	 * @param recipientId
	 * @param chatId
	 * @param cookieInfo
	 * @param totalPolls
	 */
	public static void getPollResponse(final IMSession imSession, int totalPolls) {
		final String pollPayload = String.format("{\"chatid\":\"%s\"}", imSession.getImChatId());
		logger.info("<<<<<<<<<polling Request payload::{}>>>>>>>>>>>>>>>", pollPayload);
		final RestTemplate restTemplate = new RestTemplate();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Cookie", imSession.getCookieInfo());
		final HttpEntity<String> pollEntity = new HttpEntity<>(pollPayload, headers);
		for (int i = 1; i <= totalPolls; i++) {
			final ResponseEntity<PollResponse> getPollResponseEntity = restTemplate.postForEntity(POLL_URL, pollEntity,	PollResponse.class);
			logger.info("<<<<<<<<<<<getPollResponseEntity status:::{}>>>>>>>>>>>>>>>>",	getPollResponseEntity.getStatusCode());
			final PollResponse pollResponse = getPollResponseEntity.getBody();
			logger.info("<<<<<<<<<<<<<<<<<<<<PollResponse:::{}>>>>>>>>>>>>>>>>>>>>>", pollResponse);
			logger.info("<<<<<<<<<<<<<poll No.{} => Result Collection Size:::{}>>>>>>>>>>>", i, pollResponse.getResults().size());
			logger.info("<<<<<<<<<<<<PollResponsePayload Status :::{}>>>>>>>>>>>", pollResponse.getStatus());
			for (final Result result : pollResponse.getResults()) {
				logger.info("<<<<<<<<<<<<Each Result  :::{}>>>>>>>>>>>>>", result);
				final String answer = result.getAnswer();
				final String message = result.getMessage();
				if (answer != null && !answer.isEmpty()) {
					String imResponsePayload = String.format("{ \"recipient\": { \"id\": \"%s\" }, \"message\": { \"text\": \"%s\" } }", imSession.getFbSenderId(), answer);
					logger.info("<<<<<<<<<When answer is not null, then ImResponsePayload::::{}>>>>>>>>>>", imResponsePayload);
					String fbAcknowledgement = SellaFbController.sendFBMessage(imResponsePayload);
					logger.info("***************poll Answer Acknowledgement of fb:::{}****************", fbAcknowledgement);
					if (result.getLink() != null && !result.getLink().isEmpty()) {
						imResponsePayload = String.format("{ \"recipient\":{ \"id\":\"%s\" }, \"message\":{ \"attachment\":{ \"type\":\"template\", \"payload\":{ \"template_type\":\"open_graph\", \"elements\":[ { \"url\":\"%s\", \"buttons\":[ { \"type\":\"web_url\", \"url\":\"https://www.sella.it\", \"title\":\"View More\" } ] } ] } } } }", imSession.getFbSenderId(), result.getLink());
						logger.info("<<<<<<<<<When link is not null, then ImResponsePayload::::{}>>>>>>>>>>", imResponsePayload);
						fbAcknowledgement = SellaFbController.sendFBMessage(imResponsePayload);
						logger.info("++++++++++++++++++poll link Acknowledgement of fb:::{}++++++++++++++++++",	fbAcknowledgement);
					}
				} else if (message != null && !message.isEmpty()) {
					String imResponsePayload = String.format("{ \"recipient\": { \"id\": \"%s\" }, \"message\": { \"text\": \"%s\" } }", imSession.getFbSenderId(), message);
					logger.info("<<<<<<<<<When message is not null, then ImResponsePayload::::{}>>>>>>>>>>", imResponsePayload);
					String fbAcknowledgement = SellaFbController.sendFBMessage(imResponsePayload);
					logger.info("*********************poll Message Acknowledgement of fb:::{}***************", fbAcknowledgement);
				}
			}
			try {
				Thread.sleep(new Long(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
