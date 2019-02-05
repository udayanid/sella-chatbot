package it.sella.model.im;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PollResponse {

	@SerializedName("status")
	@Expose
	private String status;
	@SerializedName("errors")
	@Expose
	private List<Object> errors = null;
	@SerializedName("results")
	@Expose
	private List<Result> results = null;
	@SerializedName("errorMessageCode")
	@Expose
	private Object errorMessageCode;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Object> getErrors() {
		return errors;
	}

	public void setErrors(List<Object> errors) {
		this.errors = errors;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	public Object getErrorMessageCode() {
		return errorMessageCode;
	}

	public void setErrorMessageCode(Object errorMessageCode) {
		this.errorMessageCode = errorMessageCode;
	}

}
