package com.webapp.exception;

@SuppressWarnings("serial")
public class UnauthorizedException extends RuntimeException {
	
	public UnauthorizedException(String msg) {
		super(msg);
	}
}
