package com.webapp.models;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Employee {
	@NotEmpty(message = "Email can not be null or empty")
	private String email;
	@NotEmpty(message = "Name can not be null or empty")
	private String name;
}
