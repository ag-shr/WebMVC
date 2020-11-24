package com.webapp.exception;

import java.util.Date;

import javax.validation.ConstraintViolationException;

import com.webapp.RequestResponseClasses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice
public class MovieBookingWebAppExceptionHandler {

    @ExceptionHandler(MovieBookingWebAppException.class)
    public ResponseEntity<ErrorMessage> handleTheaterMicroserviceException(MovieBookingWebAppException ex) {
        String message = ex.getLocalizedMessage();
        if (message == null)
            message = "";
        return new ResponseEntity<>(new ErrorMessage(new Date(), message, ex.getHttpStatus()),
          ex.getHttpStatus());
    }


    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<ErrorMessage> handleRestClientResponseException(RestClientResponseException ex) {
        String message = ex.getResponseBodyAsString();
		ObjectMapper objectMapper = new ObjectMapper();
        ErrorMessage errorMessage;
        try {
            errorMessage = objectMapper.readValue(message, ErrorMessage.class);
            return new ResponseEntity<>(errorMessage, errorMessage.getHttpStatus());
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(new ErrorMessage(new Date(),
              "Problem occurred while converting json string to object", HttpStatus.INTERNAL_SERVER_ERROR),
              HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getLocalizedMessage();
        if (message == null)
            message = e.toString();
        ErrorMessage errorMessage = new ErrorMessage(new Date(), message, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleAnyException(Exception e) {
        String message = e.getLocalizedMessage();
        if (message == null)
            message = e.toString();
        ErrorMessage errorMessage = new ErrorMessage(new Date(), message, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MicroserviceException.class)
    public ResponseEntity<ErrorResponse> serviceCallException(MicroserviceException e) {
        return new ResponseEntity<>(e.getErrorResponse(), HttpStatus.valueOf(e.getErrorResponse().getCode()));
    }

}
