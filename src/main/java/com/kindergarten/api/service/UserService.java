package com.kindergarten.api.service;

import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.model.entity.User;


public interface UserService {
    void signUpParent(User user);

    void signUpTeacher(User user);

    void signUpDirector(User user);

    SingleResult existUserId(String userid);

    User loginUser(String id, String password) throws Exception;
}
