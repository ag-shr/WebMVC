package com.webapp.services;

import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.UserNotConfirmedException;
import com.amazonaws.services.cognitoidp.model.UsernameExistsException;

import com.webapp.models.User;
import com.webapp.models.UserLoginRequestObject;

import javax.servlet.http.HttpServletResponse;

public interface UserService {

	String createUser(User user) throws UsernameExistsException;

	String loginUser(UserLoginRequestObject user, HttpServletResponse response) throws NotAuthorizedException, UserNotConfirmedException;

}
