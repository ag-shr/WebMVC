package com.moviebookingwebapp.models;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class UserLoginRequestObject {
	@NotNull(message = "Email can not be null")
	@Email(message = "Email should be in correct format")
	private String email;
	@NotNull(message = "Password can not be null")
	private String password;

	public UserLoginRequestObject() {
		super();
	}

	public UserLoginRequestObject(
			@NotNull(message = "Email can not be null") @Email(message = "Email should be in correct format") String email,
			@NotNull(message = "Password can not be null") String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
