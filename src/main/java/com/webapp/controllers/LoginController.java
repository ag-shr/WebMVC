package com.webapp.controllers;

import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.UserNotConfirmedException;
import com.webapp.exception.MovieBookingWebAppException;
import com.webapp.models.UserLoginRequestObject;
import com.webapp.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;


    @PostMapping("login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody UserLoginRequestObject user) {
        try {
            return new ResponseEntity<>(userService.loginUser(user), HttpStatus.OK);
        } catch(NotAuthorizedException e) {
            throw new MovieBookingWebAppException(e.getErrorMessage(), HttpStatus.BAD_REQUEST);
        } catch(UserNotConfirmedException e) {
            throw new MovieBookingWebAppException("Please check your registered mail and click on the provided link to verify your account", HttpStatus.BAD_REQUEST);
        }

    }


}
