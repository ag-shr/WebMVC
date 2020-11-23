package com.webapp.services;

import com.webapp.RequestResponseClasses.BookingRequest;
import com.webapp.RequestResponseClasses.BookingResponse;
import com.webapp.RequestResponseClasses.SeatPlanResponse;
import com.webapp.utilities.ServiceCallUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatPlanServiceImpl implements SeatPlanService {
	@Value("${service.seatPlan.URL}")
	private String seatPlanBaseUrl;

	@Override
	public SeatPlanResponse getSeatPlanResponse(String seatPlanId) {
		String url = seatPlanBaseUrl + seatPlanId;
		return (SeatPlanResponse) ServiceCallUtil.getForEntity(url, SeatPlanResponse.class);
	}

	@Override
	public Boolean addLockedSeats(List<String> seats, String seatPlanId, String username) {
		String url = seatPlanBaseUrl + "lockSeats/" + seatPlanId + "/user/" + username;
		return ServiceCallUtil.putList(url, List.class, seats);
	}
}
