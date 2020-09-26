package com.kindergarten.api.common.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String response;
    private String message;
    private Object data;

    public LoginResponse(String response, String message, Object data) {
        this.response = response;
        this.message = message;
        this.data = data;
    }
}
