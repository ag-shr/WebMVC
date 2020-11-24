package com.webapp.utilities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapp.RequestResponseClasses.BookingResponse;
import com.webapp.RequestResponseClasses.SeatPlanResponse;
import com.webapp.models.*;


public class MappingUtilities {

    public static String stringify(Object movie) {
        try {
            return new ObjectMapper().writeValueAsString(movie);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Can't process JSON");
        }
    }

    public static Object retrieveEntity(Object object, String entity) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (entity.equals("City")) {
                return mapper.readValue(stringify(object), new TypeReference<City>() {
                });
            } else if (entity.equals("Theater")) {
                return mapper.readValue(stringify(object), new TypeReference<Theater>() {
                });
            } else if (entity.equals("Movie")) {
                return mapper.readValue(stringify(object), new TypeReference<Movie>() {
                });
            } else if (entity.equals("Screen")) {
                return mapper.readValue(stringify(object), new TypeReference<Screen>() {
                });
            } else if (entity.equals("SeatPlanResponse")) {
                return mapper.readValue(stringify(object), new TypeReference<SeatPlanResponse>() {
                });
            } else if (entity.equals("BookingResponse")) {
                return mapper.readValue(stringify(object), new TypeReference<BookingResponse>() {
                });
            } else if (entity.equals("UserDetails")){
                return mapper.readValue(stringify(object), new TypeReference<UserDetails>() {
                });
            } else {
                throw new RuntimeException("Invalid Argument Passed");
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Can't process response");
        }
    }

}
