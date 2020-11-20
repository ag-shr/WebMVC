package com.webapp.RequestResponseClasses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
	@NotEmpty
	private String theaterDetails;
	@NotNull
	private Date dateOfShow;
	@NotEmpty
	private String showTiming;
	@NotEmpty
	private String screenName;
	@NotEmpty
	private String movieName;
	@NotNull
	@Min(50)
	private double amount;
	@NotEmpty
	private List<String> bookedSeats;
	@NotEmpty
	private String seatPlanId;
}
