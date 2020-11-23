package com.webapp.services;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.webapp.config.CognitoConfiguration;
import com.webapp.jwt.JwtConstants;
import com.webapp.models.ClientAccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.ParseException;
import java.util.Base64;

@Service
public class ClientAccessTokenServiceImpl implements ClientAccessTokenService {

	@Value("${aws.cognito.auth-domain}")
	private String authDomain;
	
	@Autowired
	private CognitoConfiguration cognitoConfig;
	
	@Autowired
	private ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor;
	
	private static final String AUTH_DOMAIN_URL = "https://%s.auth.%s.amazoncognito.com/oauth2/token";

	private String accessToken;
	
	@Override
	public String getAccessToken() {
		if(accessToken == null || this.isAccessTokenExpired()) {
			this.accessToken = this.getNewAccessToken();
		}
		System.out.println(this.accessToken);
		return this.accessToken;
	}

	private String getNewAccessToken() {
		return WebClient.builder()
				.baseUrl(getAuthDomainUrl())
				.build()
				.post()
				.uri(uriBuilder -> uriBuilder
						.queryParam("grant_type", "client_credentials")
						.queryParam("client_id", cognitoConfig.getClientId())
						.queryParam("scope", "testingcognito/application.write testingcognito/application.read")
						.build()
						)
				.header(JwtConstants.AUTHORIZATION_HEADER, JwtConstants.BASIC_TOKEN_TYPE + getAuthorizationHeader())
				.header("Content-Type", "application/x-www-form-urlencoded")
				.retrieve()
				.bodyToMono(ClientAccessToken.class)
				.block()
				.getAccess_token();
	}

	private boolean isAccessTokenExpired() {
		try {
			this.configurableJWTProcessor.process(this.accessToken, null);
		} catch (ParseException | BadJOSEException | JOSEException e) {
			if(this.tokenExpired(e)) {
				return true;
			} else {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	private boolean tokenExpired(Exception e) {
		return e.getMessage().equals(JwtConstants.EXPIRED_JWT);
	}

	private String getAuthDomainUrl() {
		return String.format(AUTH_DOMAIN_URL, authDomain, cognitoConfig.getRegion());
	}
	
	private String getAuthorizationHeader() {
		String clientIdClientSecretString = cognitoConfig.getClientId() + ":" + cognitoConfig.getClientSecret();
		return Base64.getEncoder().encodeToString(clientIdClientSecretString.getBytes());
	}
}
