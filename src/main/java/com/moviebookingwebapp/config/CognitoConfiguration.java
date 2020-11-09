package com.moviebookingwebapp.config;

import com.amazonaws.regions.Regions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;

@Configuration
public class CognitoConfiguration {

//	@Value("${amazon.aws.accesskey}")
//	private String awsAccessKey;
//
//	@Value("${amazon.aws.secretkey}")
//	private String awsSecretKey;

	@Value("${aws.cognito.endpoint}")
	private String cognitoEndpoint;
	
	@Value("${aws.cognito.region}")
	private String region;

//	@Bean
//	public AWSCredentials amazonAwsCredentials() {
//		return new BasicAWSCredentials(awsAccessKey, awsSecretKey);
//	}

	@Bean
	public AWSCognitoIdentityProvider getAmazonCognitoIdentityClient() {
		return AWSCognitoIdentityProviderClientBuilder.standard()
//				.withCredentials(new AWSStaticCredentialsProvider(amazonAwsCredentials()))
//				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(cognitoEndpoint, region))
				.withRegion(Regions.US_EAST_1)
				.build();
	}
	
//	@Bean
//	public AWSCognitoIdentityProvider getAmazonCognitoIdentityClient() {
//		return AWSCognitoIdentityProviderClientBuilder.standard()
//				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(cognitoEndpoint, region))
//				.build();
//	}
}
