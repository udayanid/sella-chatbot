package it.sella.model.im;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

	@SerializedName("sender")
	@Expose
	private String sender;
	@SerializedName("message")
	@Expose
	private String message;
	@SerializedName("answer")
	@Expose
	private String answer;
	@SerializedName("intentName")
	@Expose
	private Object intentName;
	@SerializedName("intentCode")
	@Expose
	private Object intentCode;
	@SerializedName("action")
	@Expose
	private Object action;
	@SerializedName("operatorSkill")
	@Expose
	private Object operatorSkill;
	@SerializedName("link")
	@Expose
	private Object link;
	@SerializedName("intentArea")
	@Expose
	private Object intentArea;
	@SerializedName("url")
	@Expose
	private Object url;

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Object getIntentName() {
		return intentName;
	}

	public void setIntentName(Object intentName) {
		this.intentName = intentName;
	}

	public Object getIntentCode() {
		return intentCode;
	}

	public void setIntentCode(Object intentCode) {
		this.intentCode = intentCode;
	}

	public Object getAction() {
		return action;
	}

	public void setAction(Object action) {
		this.action = action;
	}

	public Object getOperatorSkill() {
		return operatorSkill;
	}

	public void setOperatorSkill(Object operatorSkill) {
		this.operatorSkill = operatorSkill;
	}

	public Object getLink() {
		return link;
	}

	public void setLink(Object link) {
		this.link = link;
	}

	public Object getIntentArea() {
		return intentArea;
	}

	public void setIntentArea(Object intentArea) {
		this.intentArea = intentArea;
	}

	public Object getUrl() {
		return url;
	}

	public void setUrl(Object url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "Result [sender=" + sender + ", message=" + message + ", answer=" + answer + ", intentName=" + intentName
				+ ", intentCode=" + intentCode + ", action=" + action + ", operatorSkill=" + operatorSkill + ", link="
				+ link + ", intentArea=" + intentArea + ", url=" + url + "]";
	}
}