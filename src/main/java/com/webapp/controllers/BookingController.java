package com.webapp.controllers;


import com.webapp.RequestResponseClasses.BookingRequest;
import com.webapp.RequestResponseClasses.BookingResponse;
import com.webapp.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/v1/bookings")
public class BookingController {

	@Autowired
	private BookingService bookingService;

	@GetMapping
	public ResponseEntity<List<BookingResponse>> getAllBookingsForAUser(Principal principal) {
		return new ResponseEntity<>(bookingService.findAllBookingsForUser(principal.getName()), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<BookingResponse> addBooking(@Valid @RequestBody BookingRequest bookingRequest, Principal principal) {
		return new ResponseEntity<>(bookingService.addBooking(bookingRequest, principal.getName()), HttpStatus.CREATED);
	}

	@PutMapping("/{bookingId}")
	public ResponseEntity<BookingResponse> cancelBooking(@NotEmpty @PathVariable String bookingId,Principal principal) {
		return new ResponseEntity<>(bookingService.updateBooking(bookingId, principal.getName()), HttpStatus.OK);
	}

}
