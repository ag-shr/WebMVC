package com.webapp.services;

import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.UserNotConfirmedException;
import com.amazonaws.services.cognitoidp.model.UsernameExistsException;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.webapp.models.ResetPasswordRequest;
import com.webapp.models.User;
import com.webapp.models.UserLoginRequestObject;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;

public interface UserService {

	String createUser(User user) throws UsernameExistsException;

	String loginUser(UserLoginRequestObject user, HttpServletResponse response) throws NotAuthorizedException, UserNotConfirmedException, ParseException, JOSEException, BadJOSEException;

	String generateNewTokens(String userName, String lastIdToken) throws NotAuthorizedException, UserNotConfirmedException;

	void sendCodeForgotPassword(String username);

	void resetPassword(ResetPasswordRequest request);
}
