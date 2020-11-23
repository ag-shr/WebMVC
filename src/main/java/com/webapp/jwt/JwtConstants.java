package com.webapp.jwt;

import com.webapp.config.CognitoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtConstants {

	@Autowired
	private CognitoConfiguration cognitoConfig;
	
	public static final String AUTHORIZATION_HEADER = "Authorization";
	
	public static final String BEARER_TOKEN_TYPE = "Bearer ";
	
	public static final String BASIC_TOKEN_TYPE = "Basic ";
	
	public static final String USERNAME_FIELD = "cognito:username";
	
	public static final String ROLES = "cognito:groups";
	
	public static final String EMAIL_CLAIM = "email";
	
	public static final String PHONE_NUMBER_CLAIM = "phone_number";
	
	public static final String AUTH_COOKIE_NAME = "auth_token";
	
	public static final String EMAIL_VERIFIED = "email_verified";
	
	public static final String EXPIRED_JWT = "Expired JWT";
	
	public static final String OAUTH_COOKIE_NAME = "auth_token";

	public String getIssuer() {
		return String.format("https://%s/%s", cognitoConfig.getCognitoEndpoint(), cognitoConfig.getUserPoolId());
	}

	public String getWellKnownJwkUrl() {
		return String.format("%s/.well-known/jwks.json", this.getIssuer());
	}
}
