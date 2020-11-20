package com.webapp.services;

import com.webapp.RequestResponseClasses.BookingRequest;
import com.webapp.RequestResponseClasses.BookingResponse;
import com.webapp.utilities.ServiceCallUtil;
import org.springframework.http.HttpMethod;

import java.util.List;

public class BookingServiceImpl implements BookingService {
	private final String bookingBaseUrl = "http://localhost:8085/v1/bookings/";

	@Override
	public List<BookingResponse> findAllBookingsForUser(String username) {
		return (List<BookingResponse>) ServiceCallUtil.getForList(bookingBaseUrl + "user/" + username, BookingResponse.class);
	}

	@Override
	public BookingResponse addBooking(BookingRequest bookingRequest, String username) {
		return (BookingResponse) ServiceCallUtil.postPutForEntity(bookingBaseUrl+ "user/" + username
		  , HttpMethod.POST, BookingRequest.class
		  , BookingResponse.class, bookingRequest);
	}

	@Override
	public BookingResponse updateBooking(String bookingId, String username) {
		return (BookingResponse) ServiceCallUtil.postPutForEntity(bookingBaseUrl + bookingId + "/user/" + username
		  , HttpMethod.PUT, Void.class
		  , BookingResponse.class,null );
	}
}
