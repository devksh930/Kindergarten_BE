package com.kindergarten.api.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestLoginUser {
    private String userid;
    private String password;

    public RequestLoginUser(String userid, String password) {
        this.userid = userid;
        this.password = password;
    }
}
