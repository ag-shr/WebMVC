package com.moviebookingwebapp.exception;

import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice
public class MovieBookingWebAppExceptionHandler {

	@ExceptionHandler(value = { MovieBookingWebAppException.class })
	public ResponseEntity<ErrorMessage> handleTheaterMicroserviceException(MovieBookingWebAppException ex) {
		String message = ex.getLocalizedMessage();
		if (message == null)
			message = "";
		return new ResponseEntity<ErrorMessage>(new ErrorMessage(new Date(), message, ex.getHttpStatus()),
				ex.getHttpStatus());
	}

	@ExceptionHandler(value = { InterServiceCallException.class })
	public ResponseEntity<ErrorMessage> handleInterServiceCallException(InterServiceCallException ex) {
		String message = ex.getLocalizedMessage();
		if (message == null)
			message = "";
		ObjectMapper objectMapper = new ObjectMapper();
		ErrorMessage errorMessage;
		try {
			errorMessage = objectMapper.readValue(message, ErrorMessage.class);
			return new ResponseEntity<ErrorMessage>(errorMessage, errorMessage.getHttpStatus());
		} catch (JsonProcessingException e) {
			return new ResponseEntity<ErrorMessage>(new ErrorMessage(new Date(),
					"Problem occurred while converting json string to object", HttpStatus.INTERNAL_SERVER_ERROR),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ExceptionHandler(value = { RestClientResponseException.class })
	public ResponseEntity<ErrorMessage> handleRestClientResponseException(RestClientResponseException ex) {
		String message = ex.getResponseBodyAsString();
		if (message == null)
			message = "";
		ObjectMapper objectMapper = new ObjectMapper();
		ErrorMessage errorMessage;
		try {
			errorMessage = objectMapper.readValue(message, ErrorMessage.class);
			return new ResponseEntity<ErrorMessage>(errorMessage, errorMessage.getHttpStatus());
		} catch (JsonProcessingException e) {
			return new ResponseEntity<ErrorMessage>(new ErrorMessage(new Date(),
					"Problem occurred while converting json string to object", HttpStatus.INTERNAL_SERVER_ERROR),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ExceptionHandler(value = { ConstraintViolationException.class })
	public ResponseEntity<ErrorMessage> handleConstraintViolationException(ConstraintViolationException e) {
		String message = e.getLocalizedMessage();
		if (message == null)
			message = e.toString();
		ErrorMessage errorMessage = new ErrorMessage(new Date(), message, HttpStatus.BAD_REQUEST);
		return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<ErrorMessage> handleAnyException(Exception e) {
		String message = e.getLocalizedMessage();
		if (message == null)
			message = e.toString();
		ErrorMessage errorMessage = new ErrorMessage(new Date(), message, HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<ErrorMessage>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
