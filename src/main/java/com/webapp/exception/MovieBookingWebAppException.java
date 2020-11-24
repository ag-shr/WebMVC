package com.webapp.exception;

import org.springframework.http.HttpStatus;

public class MovieBookingWebAppException extends RuntimeException {

	private HttpStatus httpStatus;

	public MovieBookingWebAppException(MovieBookingWebAppException e, HttpStatus httpStatus) {
		super(e);
		this.httpStatus = httpStatus;
	}

	public MovieBookingWebAppException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public MovieBookingWebAppException(MovieBookingWebAppException e, String message, HttpStatus httpStatus) {
		super(message, e);
		this.httpStatus = httpStatus;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

}
