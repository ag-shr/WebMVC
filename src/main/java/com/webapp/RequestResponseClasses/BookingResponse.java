package com.webapp.RequestResponseClasses;

import com.webapp.enums.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
	private String bookingId;
	private String theaterDetails;
	private Date dateOfShow;
	private Date dateOfBooking;
	private String showTiming;
	private String screenName;
	private String movieName;
	private double amount;
	private List<String> bookedSeats;
	private String transactionId;
	private String seatPlanId;
	private BookingStatus bookingStatus;
}
