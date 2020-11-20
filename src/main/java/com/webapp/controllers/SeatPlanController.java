package com.webapp.controllers;

import com.webapp.RequestResponseClasses.SeatPlanResponse;
import com.webapp.services.SeatPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/seats")
public class SeatPlanController {

	@Autowired
	private SeatPlanService seatPlanService;

	@GetMapping("/{seatPlanId}")
	public ResponseEntity<SeatPlanResponse> getSeatPlans(@PathVariable String seatPlanId) {
		return new ResponseEntity<>(seatPlanService.getSeatPlanResponse(seatPlanId), HttpStatus.OK);
	}

	@PutMapping("/lockSeats/{seatPlanId}")
	public ResponseEntity<Boolean> addLockedSeats(@NotEmpty @RequestBody List<String> seats, @PathVariable String seatPlanId, Principal principal) {
		return new ResponseEntity<Boolean>(seatPlanService.addLockedSeats(seats, seatPlanId, principal.getName()), HttpStatus.OK);
	}
}