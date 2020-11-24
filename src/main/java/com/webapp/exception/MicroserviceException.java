package com.webapp.exception;

import com.webapp.RequestResponseClasses.ErrorResponse;

public class MicroserviceException extends RuntimeException {

    private ErrorResponse errorResponse;

    public MicroserviceException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

}
