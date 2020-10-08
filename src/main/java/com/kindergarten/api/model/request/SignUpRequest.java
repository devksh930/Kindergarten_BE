package com.kindergarten.api.model.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class SignUpRequest {
    @NotEmpty
    private String userid;
    @NotEmpty
    private String password;
    @NotEmpty
    private String name;
    @NotEmpty
    private String phone;
    @Email
    private String email;

}
