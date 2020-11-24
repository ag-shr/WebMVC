package com.webapp.controllers;

import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.UserNotConfirmedException;
import com.amazonaws.services.cognitoidp.model.UsernameExistsException;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.webapp.exception.MovieBookingWebAppException;
import com.webapp.models.*;
import com.webapp.services.CognitoUserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;

@Controller
public class LoginController {

    private final CognitoUserService cognitoUserService;

    public LoginController(CognitoUserService cognitoUserService) {
        this.cognitoUserService = cognitoUserService;
    }

    @PostMapping(value = "login", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    public ResponseEntity<String> loginUser(@Valid UserLoginRequestObject user, HttpServletResponse response) {
        try {
            return new ResponseEntity<>(cognitoUserService.loginUser(user, response), HttpStatus.OK);
        } catch (NotAuthorizedException | ParseException | JOSEException | BadJOSEException e) {
            throw new MovieBookingWebAppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UserNotConfirmedException e) {
            throw new MovieBookingWebAppException("Please check your registered mail and click on the provided link to verify your account", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "signUp", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    public ResponseEntity<UserDetails> registerUser(@Valid User user) {
        try {
            return new ResponseEntity<>(cognitoUserService.createUser(user), HttpStatus.OK);
        } catch (UsernameExistsException e) {
            throw new MovieBookingWebAppException(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("forgotPassword")
    @ResponseBody
    public String forgotPassword(@RequestParam("email") String username, HttpServletResponse response) throws IOException {
        String result = cognitoUserService.forgotPassword(username);
        if(result.equals("resetPassword"))
            response.sendRedirect("resetPassword");
        return result;
    }

    @PostMapping(value = "reset", consumes = "application/x-www-form-urlencoded")
    public void resetPassword(ResetPasswordRequest request, HttpServletResponse response) throws IOException {
        cognitoUserService.resetPassword(request);
        response.sendRedirect("login");
    }

    @PostMapping(value = "changePassword", consumes = "application/x-www-form-urlencoded")
    public void changePassword(UserChangePasswordRequest request, HttpServletResponse response) throws IOException {
        cognitoUserService.changePassword(request);
    }

    @GetMapping(path = "logoutUser")
    public void logout(Principal principal, HttpServletResponse response) throws IOException {
        if(principal==null)
            response.sendRedirect("");
        cognitoUserService.logout(principal,response);
        response.sendRedirect("login");
    }

}
