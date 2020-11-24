package com.webapp.controllers;

import com.webapp.models.UserDetails;
import com.webapp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/v1/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<UserDetails> getUserDetails(Principal principal) {
		return new ResponseEntity<UserDetails>(userService.getUserDetails(principal.getName()), HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<UserDetails> updateUserDetails(@Valid @RequestBody UserDetails userDetails) {
		return new ResponseEntity<UserDetails>(userService.updateUserDetails(userDetails), HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteUserDetails(Principal principal) {
		userService.deleteUserDetails(principal.getName());
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
