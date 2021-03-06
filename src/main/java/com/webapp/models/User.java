package com.webapp.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@NotNull(message = "Name can not be null")
	private String name;

	@NotNull(message = "Email can not be null")
	@Email(message = "Email should be in correct format")
	private String email;

	@NotNull(message = "Password can not be null")
	private String password;

	private String phoneNumber;

	private Date dateOfBirth;

}
