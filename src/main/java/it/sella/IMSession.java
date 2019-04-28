package it.sella;

import java.time.LocalDateTime;

public class IMSession {

	private String fbSenderId;
	private String fbReceipientId;
	private String imChatId;
	private String cookieInfo;
	private LocalDateTime lastRequestDate;	

	public LocalDateTime getLastRequestDate() {
		return lastRequestDate;
	}

	public void setLastRequestDate(LocalDateTime lastRequestDate) {
		this.lastRequestDate = lastRequestDate;
	}	

	public String getCookieInfo() {
		return cookieInfo;
	}

	public void setCookieInfo(String cookieInfo) {
		this.cookieInfo = cookieInfo;
	}

	public String getFbSenderId() {
		return fbSenderId;
	}

	public void setFbSenderId(String fbSenderId) {
		this.fbSenderId = fbSenderId;
	}

	public String getFbReceipientId() {
		return fbReceipientId;
	}

	public void setFbReceipientId(String fbReceipientId) {
		this.fbReceipientId = fbReceipientId;
	}

	public String getImChatId() {
		return imChatId;
	}

	public void setImChatId(String imChatId) {
		this.imChatId = imChatId;
	}

	@Override
	public String toString() {
		return "IMSession [fbSenderId=" + fbSenderId + ", fbReceipientId=" + fbReceipientId + ", imChatId=" + imChatId
				+ ", cookieInfo=" + cookieInfo + ", lastRequestDate=" + lastRequestDate + "]";
	}
	
}
