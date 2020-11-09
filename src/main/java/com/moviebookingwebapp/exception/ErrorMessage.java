package com.moviebookingwebapp.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;

public class ErrorMessage {
	private Date timestamp;
	private String errorMessage;
	private HttpStatus httpStatus;

	public ErrorMessage() {
		super();
	}

	public ErrorMessage(Date timestamp, String errorMessage, HttpStatus httpStatus) {
		super();
		this.timestamp = timestamp;
		this.errorMessage = errorMessage;
		this.httpStatus = httpStatus;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

}
