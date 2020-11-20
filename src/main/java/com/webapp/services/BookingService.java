package com.webapp.services;

import com.webapp.RequestResponseClasses.BookingRequest;
import com.webapp.RequestResponseClasses.BookingResponse;

import java.util.List;

public interface BookingService {

    List<BookingResponse> findAllBookingsForUser(String username);

    BookingResponse addBooking(BookingRequest bookingRequest, String username);

    BookingResponse updateBooking(String bookingId, String username);

}
