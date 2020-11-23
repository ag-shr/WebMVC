package com.webapp.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jose.util.DefaultResourceRetriever;
import com.nimbusds.jose.util.ResourceRetriever;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import com.webapp.jwt.JwtConfiguration;

import com.webapp.services.ClientAccessTokenService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;
import java.net.URL;

import static com.nimbusds.jose.JWSAlgorithm.RS256;

@Configuration
@Data
public class CognitoConfiguration {

	@Value("${aws.cognito.endpoint}")
	private String cognitoEndpoint;

	@Value("${aws.cognito.region}")
	private String region;

	@Value("${aws.cognito.clientId}")
	private String clientId;

	@Value("${aws.cognito.clientSecret}")
	private String clientSecret;

	@Value("${aws.cognito.userPoolId}")
	private String userPoolId;

	private final JwtConfiguration jwtConfiguration;

	public CognitoConfiguration(JwtConfiguration jwtConfiguration) {
		this.jwtConfiguration = jwtConfiguration;
	}



	@Bean
	public AWSCognitoIdentityProvider getAmazonCognitoIdentityClient() {
		return AWSCognitoIdentityProviderClientBuilder.standard()
		  .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(cognitoEndpoint, region))
		  .build();
	}

	@Bean
	public ConfigurableJWTProcessor<SecurityContext> configurableJWTProcessor() throws MalformedURLException {
		ResourceRetriever resourceRetriever = new DefaultResourceRetriever(2000, 2000);
		URL jwkURL = new URL(jwtConfiguration.getJwkUrl());
		JWKSource<SecurityContext> keySource = new RemoteJWKSet<>(jwkURL, resourceRetriever);
		ConfigurableJWTProcessor<SecurityContext> jwtProcessor = new DefaultJWTProcessor<>();
		JWSKeySelector<SecurityContext> keySelector = new JWSVerificationKeySelector<>(RS256, keySource);
		jwtProcessor.setJWSKeySelector(keySelector);
		return jwtProcessor;
	}

}
