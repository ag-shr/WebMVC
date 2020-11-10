package com.webapp.controllers;

import javax.validation.Valid;

import com.webapp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.UserNotConfirmedException;
import com.amazonaws.services.cognitoidp.model.UsernameExistsException;
import com.webapp.exception.MovieBookingWebAppException;
import com.webapp.models.UserLoginRequestObject;
import com.webapp.services.UserService;


@Controller
@RequestMapping(path = "/v1/main")
public class MainController {

	@Autowired
	private UserService userService;
	
	@PostMapping(path = "/signUpDataRequest")
	public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
		try {
			return new ResponseEntity<String>(userService.createUser(user), HttpStatus.OK);
		}catch(UsernameExistsException e) {
			throw new MovieBookingWebAppException(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}

	@GetMapping(path = "/signUpPageRequest")
	public String getRegisterPage() {
		return "register";
	}
	
	@PostMapping(path = "/signInDataRequest")
	public ResponseEntity<String> loginUser(@Valid @RequestBody UserLoginRequestObject user) {
		try {
			return new ResponseEntity<String>(userService.loginUser(user), HttpStatus.OK);
		}catch(NotAuthorizedException e) {
			throw new MovieBookingWebAppException(e.getErrorMessage(), HttpStatus.BAD_REQUEST);
		}catch(UserNotConfirmedException e) {
			throw new MovieBookingWebAppException("Please check your registered mail and click on the provided link to verify your account", HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(path = "/signInDataRequest")
	public String getLoginPage() {
		return "login";
	}
	
	//health check added
	@GetMapping(path = "/health")
	public ResponseEntity<Boolean> healthCheck() {
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
	}
}
