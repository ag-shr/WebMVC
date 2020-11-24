package com.webapp.services;

import com.webapp.RequestResponseClasses.SeatPlanResponse;
import com.webapp.utilities.MappingUtilities;
import com.webapp.utilities.ResponseHandler;
import com.webapp.utilities.ServiceCallUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatPlanServiceImpl implements SeatPlanService {

	@Value("${service.seatPlan.URL}")
	private String seatPlanBaseUrl;

	@Override
	public SeatPlanResponse getSeatPlanResponse(String seatPlanId) {
		String url = seatPlanBaseUrl + seatPlanId;
		Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.get(url));
		return (SeatPlanResponse) MappingUtilities.retrieveEntity(response, "SeatPlanResponse");
	}

	@Override
	public Boolean addLockedSeats(List<String> seats, String seatPlanId, String username) {
		String url = seatPlanBaseUrl + "lockSeats/" + seatPlanId + "/user/" + username;
		Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.putList(url, List.class, seats));
		return (Boolean) response;
	}
}
