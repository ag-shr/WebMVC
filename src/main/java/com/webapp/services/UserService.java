package com.webapp.services;

import com.webapp.models.UserDetails;

public interface UserService {
	UserDetails getUserDetails(String userEmail);

	UserDetails addUserDetails(UserDetails userDetails);

	UserDetails updateUserDetails(UserDetails userDetails);

	void deleteUserDetails(String userEmail);
}
