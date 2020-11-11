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
@RequestMapping(path = "/v1")
public class MainController {

	@Autowired
	private UserService userService;

	@PostMapping(path = "/main/signUpDataRequest")
	public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
		try {
			return new ResponseEntity<String>(userService.createUser(user), HttpStatus.OK);
		}catch(UsernameExistsException e) {
			throw new MovieBookingWebAppException(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}

	@GetMapping(path = "/main/signUpPageRequest")
	public String getRegisterPage() {
		return "register";
	}

	
	@GetMapping(path = "/main/signInDataRequest")
	public String getLoginPage() {
		return "login";
	}
	
	//health check added
	@GetMapping(path = "/main/health")
	public ResponseEntity<Boolean> healthCheck() {
		return new ResponseEntity<>(true, HttpStatus.OK);
	}
}
