package com.webapp.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Email;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "UserDetails")
public class UserDetails {
	@NonNull
	@DynamoDBAttribute
	private String fullName;
	@Email
	@DynamoDBHashKey
	private String email;
	@DynamoDBAttribute
	private String phoneNumber;
	@DynamoDBAttribute
	private Date dateOfBirth;
}
