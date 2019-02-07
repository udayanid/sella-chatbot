package it.sella.model.im;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author gbs04423
 *
 */
public class ImResponse {

	@SerializedName("status")
	@Expose
	private String status;
	@SerializedName("errors")
	@Expose
	private List<Error> errors = null;
	@SerializedName("requests")
	@Expose
	private Object requests;
	@SerializedName("codes")
	@Expose
	private Object codes;
	@SerializedName("contextChange")
	@Expose
	private Object contextChange;
	@SerializedName("concepts")
	@Expose
	private Object concepts;
	@SerializedName("favorites")
	@Expose
	private Object favorites;
	@SerializedName("chatid")
	@Expose
	private String chatid;
	@SerializedName("chaturl")
	@Expose
	private String chaturl;
	@SerializedName("result")
	@Expose
	private String result;//OK
	@SerializedName("cause")
	@Expose
	private Object cause;
	@SerializedName("licenses")
	@Expose
	private Object licenses;
	@SerializedName("transcript")
	@Expose
	private Object transcript;
	@SerializedName("overTime")
	@Expose
	private String overTime;
	@SerializedName("errorMessageCode")
	@Expose
	private String errorMessageCode;
	@SerializedName("results")
	@Expose
	private List<Result> results = null;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<Error> getErrors() {
		return errors;
	}
	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}
	public Object getRequests() {
		return requests;
	}
	public void setRequests(Object requests) {
		this.requests = requests;
	}
	public Object getCodes() {
		return codes;
	}
	public void setCodes(Object codes) {
		this.codes = codes;
	}
	public Object getContextChange() {
		return contextChange;
	}
	public void setContextChange(Object contextChange) {
		this.contextChange = contextChange;
	}
	public Object getConcepts() {
		return concepts;
	}
	public void setConcepts(Object concepts) {
		this.concepts = concepts;
	}
	public Object getFavorites() {
		return favorites;
	}
	public void setFavorites(Object favorites) {
		this.favorites = favorites;
	}
	public String getChatid() {
		return chatid;
	}
	public void setChatid(String chatid) {
		this.chatid = chatid;
	}
	public String getChaturl() {
		return chaturl;
	}
	public void setChaturl(String chaturl) {
		this.chaturl = chaturl;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public Object getCause() {
		return cause;
	}
	public void setCause(Object cause) {
		this.cause = cause;
	}
	public Object getLicenses() {
		return licenses;
	}
	public void setLicenses(Object licenses) {
		this.licenses = licenses;
	}
	public Object getTranscript() {
		return transcript;
	}
	public void setTranscript(Object transcript) {
		this.transcript = transcript;
	}
	public String getOverTime() {
		return overTime;
	}
	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}
	public String getErrorMessageCode() {
		return errorMessageCode;
	}
	public void setErrorMessageCode(String errorMessageCode) {
		this.errorMessageCode = errorMessageCode;
	}
	public List<Result> getResults() {
		return results;
	}
	public void setResults(List<Result> results) {
		this.results = results;
	}
	
	@Override
	public String toString() {
		return "ImResponse [status=" + status + ", errors=" + errors + ", requests=" + requests + ", codes=" + codes
				+ ", contextChange=" + contextChange + ", concepts=" + concepts + ", favorites=" + favorites
				+ ", chatid=" + chatid + ", chaturl=" + chaturl + ", result=" + result + ", cause=" + cause
				+ ", licenses=" + licenses + ", transcript=" + transcript + ", overTime=" + overTime
				+ ", errorMessageCode=" + errorMessageCode + ", results=" + results + "]";
	}

	
	//{ "status":"EXCEPTION", "errors":[ { "messageCode":"IM_CHAT_ID_NOT_FOUND", "messageParams":[  ], "messageFEFields":"*" } ], "requests":null, "codes":null, "contextChange":null, "concepts":null, "favorites":null, "chatid":null, "chaturl":null, "result":null, "cause":null, "licenses":null, "transcript":null, "overTime":null, "errorMessageCode":"IM_CHAT_ID_NOT_FOUND" }
	
	//"results":[{"sender":"BOT","message":"Hai bisogno di informazioni sul suo conto corrente, su un bonifico, su un F24 o vuoi informazioni sulle carte?","answer":null,"intentName":null,"intentCode":null,"action":null,"operatorSkill":null,"link":null,"intentArea":null,"url":null}]
	

}
