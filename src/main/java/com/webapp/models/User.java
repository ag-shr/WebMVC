package com.webapp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

//	@NotNull(message = "Mobile number can not be null")
//	@Size(min = 10, max = 10)
//	private String mobile;
//	private String profilePicture;
//	@NotNull(message = "First name can not be null")
//	private String firstName;
//	@NotNull(message = "Last name can not be null")
//	private String lastName;
//	@NotNull(message = "DOB can not be null")
//	private String dob;
//	@NotNull(message = "Gender can not be null")
//	private String gender;
//	@NotNull(message = "Role can not be null")
//	private String role;
}
