package com.webapp.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientAccessToken {

	private String access_token;
	
	private Integer expires_in;
	
	private String token_type;
}
