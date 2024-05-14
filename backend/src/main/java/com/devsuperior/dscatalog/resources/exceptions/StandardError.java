package com.devsuperior.dscatalog.resources.exceptions;

import java.io.Serializable;
import java.time.Instant;

public class StandardError implements Serializable {
	
	private Instant timestamp;
	private Integer status;
	private String error;
	private String message;
	private String path;
	
	
	public StandardError() {		
	}


	public Instant getTimestamp() {
		return timestamp;
	}


	public Integer getStatus() {
		return status;
	}


	public String getError() {
		return error;
	}


	public String getPath() {
		return path;
	}


	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}


	public void setStatus(Integer status) {
		this.status = status;
	}


	public void setError(String error) {
		this.error = error;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}
	
}
