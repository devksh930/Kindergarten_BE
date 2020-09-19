package com.kindergarten.api.service;

import com.kindergarten.api.model.entity.User;

import java.lang.reflect.Member;

public interface AuthService {
    void signUpParent(User user);

    void signUpTeacher(User user);

    void signUpDirector(User user);

    User loginUser(String id, String password) throws Exception;
}
