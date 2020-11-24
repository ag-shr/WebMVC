package com.webapp.utilities;

import com.webapp.RequestResponseClasses.MicroserviceResponse;
import com.webapp.exception.MicroserviceException;

public class ResponseHandler {

    public static Object handleServiceResponse(MicroserviceResponse response) {
        if (response.getStatus() >= 200 && response.getStatus() <= 204) {
            return response.getPayload().getResponse();
        }
        else {
            throw new MicroserviceException(response.getPayload().getException());
        }
    }
}
