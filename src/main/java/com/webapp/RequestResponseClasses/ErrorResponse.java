package com.webapp.RequestResponseClasses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResponse {

    private String errorMsg;
    private String code;
    private String cause;

}
