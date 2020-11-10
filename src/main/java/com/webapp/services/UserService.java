package com.webapp.services;

import com.amazonaws.services.cognitoidp.model.NotAuthorizedException;
import com.amazonaws.services.cognitoidp.model.UserNotConfirmedException;
import com.amazonaws.services.cognitoidp.model.UsernameExistsException;
import com.webapp.models.User;
import com.webapp.models.UserLoginRequestObject;

public interface UserService {

	public String createUser(User user) throws UsernameExistsException;

	public String loginUser(UserLoginRequestObject user) throws NotAuthorizedException, UserNotConfirmedException;

}
