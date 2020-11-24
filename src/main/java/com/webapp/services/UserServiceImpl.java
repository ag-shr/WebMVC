package com.webapp.services;

import com.webapp.models.UserDetails;
import com.webapp.utilities.MappingUtilities;
import com.webapp.utilities.ResponseHandler;
import com.webapp.utilities.ServiceCallUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

	@Value("${service.userDetails.URL}")
	private String userDetailsBaseUrl;

	@Override
	public UserDetails getUserDetails(String userEmail) {
		Object response = ResponseHandler.handleServiceResponse(ServiceCallUtil.get(userDetailsBaseUrl + userEmail));
		return (UserDetails) MappingUtilities.retrieveEntity(response, "UserDetails");
	}

	@Override
	public UserDetails addUserDetails(UserDetails userDetails) {
		return (UserDetails) ServiceCallUtil.postPutForEntity(userDetailsBaseUrl, HttpMethod.POST,UserDetails.class,UserDetails.class,userDetails);
	}

	@Override
	public UserDetails updateUserDetails(UserDetails userDetails) {
		return (UserDetails) ServiceCallUtil.postPutForEntity(userDetailsBaseUrl, HttpMethod.PUT,UserDetails.class,UserDetails.class,userDetails);
	}

	@Override
	public void deleteUserDetails(String userEmail) {
		ServiceCallUtil.delete(userDetailsBaseUrl+userEmail);
	}
}
