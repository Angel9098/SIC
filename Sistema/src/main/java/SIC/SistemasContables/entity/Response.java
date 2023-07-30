package SIC.SistemasContables.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Response {

	private boolean status;
	private boolean error;
	private String message;
	private String exception;
	private String token;
	private String url;
	private List<Object> dataset;

	public Response() {
		this.status = false;
		this.error = false;
		this.message = null;
		this.exception = null;
		this.url = null;
		this.dataset = new ArrayList<Object>();
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Object> getDataset() {
		return dataset;
	}

	public void setDataset(List<Object> dataset) {
		this.dataset = dataset;
	}
	
	
}
