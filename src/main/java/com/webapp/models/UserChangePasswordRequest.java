package com.webapp.models;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class UserChangePasswordRequest {

	private String prevPassword;
	private String proposedPassword;
}
