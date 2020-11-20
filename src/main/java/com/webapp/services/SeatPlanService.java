package com.webapp.services;

import com.webapp.RequestResponseClasses.SeatPlanResponse;

import java.util.List;

public interface SeatPlanService {

	SeatPlanResponse getSeatPlanResponse(String seatPlanId);

	Boolean addLockedSeats(List<String> seats, String seatPlanId, String username);
}
