package com.webapp.services;

import com.webapp.RequestResponseClasses.BookingRequest;
import com.webapp.RequestResponseClasses.BookingResponse;
import com.webapp.RequestResponseClasses.SeatPlanResponse;
import com.webapp.utilities.ServiceCallUtil;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatPlanServiceImpl implements SeatPlanService {
	private final String seatPlanBaseUrl = "http://localhost:8085/v1/seats/";

	@Override
	public SeatPlanResponse getSeatPlanResponse(String seatPlanId) {
		String url = seatPlanBaseUrl + seatPlanId;;
		return null;
	}

	@Override
	public Boolean addLockedSeats(List<String> seats, String seatPlanId, String username) {
		String url = seatPlanBaseUrl + seatPlanId + "/user/" + username;
		return null;
	}
}
