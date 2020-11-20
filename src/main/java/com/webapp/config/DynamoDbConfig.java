package com.webapp.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

import com.webapp.models.UserTokens;

import lombok.extern.log4j.Log4j2;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Log4j2
@Configuration
@EnableDynamoDBRepositories(basePackages = "com.webapp.repository")
public class DynamoDbConfig {

	@Value("${dynamodb.end-point.url}")
	private String awsDynamoDBEndPoint;

	@Value("${dynamodb.region}")
	private String region;
	
	@Value("${amazon.aws.accesskey}")
    private String awsAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String awsSecretKey;

    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(awsAccessKey, awsSecretKey);
    }

    public AWSCredentialsProvider amazonAWSCredentialsProvider() {
        return new AWSStaticCredentialsProvider(amazonAWSCredentials());
    }
	
	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
		return AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(awsDynamoDBEndPoint, region))
				.withCredentials(amazonAWSCredentialsProvider())
				.build();
	}

	@PostConstruct
	private void setupTables() {
		AmazonDynamoDB amazonDynamoDB = amazonDynamoDB();
		DynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDB);
		CreateTableRequest authCredentials = mapper.generateCreateTableRequest(UserTokens.class);
		authCredentials.setProvisionedThroughput(new ProvisionedThroughput(5L, 5L));
		if (TableUtils.createTableIfNotExists(amazonDynamoDB, authCredentials))
			log.info("Created Table");
	}
}
