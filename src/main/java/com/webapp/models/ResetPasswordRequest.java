package com.webapp.models;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String username;
    private String password;
    private String code;

}
