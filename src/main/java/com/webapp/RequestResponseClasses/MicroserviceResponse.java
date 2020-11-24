package com.webapp.RequestResponseClasses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MicroserviceResponse {

    @Data
    @NoArgsConstructor
    public static class Payload {
        private Object response;
        private ErrorResponse exception;
    }

    private int status;
    private Payload payload;

}