package com.webapp.services;

import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.UserNotConfirmedException;
import com.amazonaws.services.cognitoidp.model.UsernameExistsException;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.webapp.models.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.text.ParseException;

public interface CognitoUserService {

	UserDetails createUser(User user) throws UsernameExistsException;

	String loginUser(UserLoginRequestObject user, HttpServletResponse response) throws NotAuthorizedException, UserNotConfirmedException, ParseException, JOSEException, BadJOSEException;

	String generateNewTokens(String userName,String userEmail, String lastIdToken) throws NotAuthorizedException, UserNotConfirmedException;

	String forgotPassword(String username);

	void resetPassword(ResetPasswordRequest request);

	void logout(Principal principal, HttpServletResponse response);

	Boolean changePassword(UserChangePasswordRequest changePasswordRequest);
}
