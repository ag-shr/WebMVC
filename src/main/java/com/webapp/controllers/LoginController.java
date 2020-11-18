package com.webapp.controllers;

import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.UserNotConfirmedException;
import com.amazonaws.services.cognitoidp.model.UsernameExistsException;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.webapp.exception.MovieBookingWebAppException;
import com.webapp.models.ResetPasswordRequest;
import com.webapp.models.User;
import com.webapp.models.UserLoginRequestObject;
import com.webapp.services.UserService;

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

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "signUp")
    public String getRegisterPage(Principal principal) {
        if(principal!=null)
            return "index";
        return "register";
    }

    @GetMapping(path = "login")
    public String getLoginPage(Principal principal) {
        if(principal!=null)
            return "index";
        return "login";
    }

    @PostMapping(value = "login", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    public ResponseEntity<String> loginUser(@Valid UserLoginRequestObject user, HttpServletResponse response) {
        try {
            return new ResponseEntity<>(userService.loginUser(user, response), HttpStatus.OK);
        } catch (NotAuthorizedException | ParseException | JOSEException | BadJOSEException e) {
            throw new MovieBookingWebAppException(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (UserNotConfirmedException e) {
            throw new MovieBookingWebAppException("Please check your registered mail and click on the provided link to verify your account", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(path = "signUp", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    public ResponseEntity<String> registerUser(@Valid User user) {
        System.out.println(user.getEmail());
        try {
            return new ResponseEntity<>(userService.createUser(user), HttpStatus.OK);
        } catch (UsernameExistsException e) {
            throw new MovieBookingWebAppException(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("forgot")
    public String forgot(Principal principal) {
        if(principal!=null)
            return "index";
        return "forgotPassword";
    }

    @GetMapping("reset")
    public String reset(Principal principal) {
        if(principal!=null)
            return "index";
        return "resetPassword";
    }

    @GetMapping("forgotPassword")
    public String forgotPassword(@RequestParam("email") String username, HttpServletResponse response) throws IOException {
        return userService.forgotPassword(username);
    }

    @PostMapping(value = "reset", consumes = "application/x-www-form-urlencoded")
    public void resetPassword(ResetPasswordRequest request, HttpServletResponse response) throws IOException {
        userService.resetPassword(request);
        response.sendRedirect("login");
    }

    @GetMapping(path = "logout")
    public void logout(Principal principal, HttpServletResponse response) throws IOException {
        System.out.println("erfsdfsdfsdf");
        userService.logout(principal,response);
        System.out.println(principal.getName());

    }

}
