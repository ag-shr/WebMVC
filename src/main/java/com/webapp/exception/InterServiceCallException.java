package com.webapp.exception;

import org.springframework.http.HttpStatus;

public class InterServiceCallException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6444494838198789606L;
	private HttpStatus httpStatus;

	public InterServiceCallException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}
	
	public InterServiceCallException(String message) {
		super(message);
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
}
