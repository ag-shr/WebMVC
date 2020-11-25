package com.webapp.services;

import com.webapp.RequestResponseClasses.BookingRequest;
import com.webapp.RequestResponseClasses.BookingResponse;
import com.webapp.utilities.MappingUtilities;
import com.webapp.utilities.ResponseHandler;
import com.webapp.utilities.ServiceCallUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    @Value("${service.booking.URL}")
    private String bookingBaseUrl;

    @Override
    public List<BookingResponse> findAllBookingsForUser(String username) {
        Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.get(bookingBaseUrl + "user/" + username));
        return (List<BookingResponse>) response;
    }

    @Override
    public BookingResponse addBooking(BookingRequest bookingRequest, String username) {
        Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.postPutForEntity(bookingBaseUrl
          + "user/" + username, HttpMethod.POST, BookingRequest.class, bookingRequest));

        return (BookingResponse) MappingUtilities.retrieveEntity(response, "BookingResponse");
    }

    @Override
    public BookingResponse updateBooking(String bookingId, String username) {
        Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.postPutForEntity(bookingBaseUrl
          + bookingId + "/user/" + username, HttpMethod.PUT, Void.class, Void.TYPE));

        return (BookingResponse) MappingUtilities.retrieveEntity(response, "BookingResponse");
    }
}
