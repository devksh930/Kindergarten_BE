package com.kindergarten.api.users;

public enum UserRole {
    //USER=일반, PARENT=학생을 등록한 학부모, TEACHER=선생님, DIRECTOR= 원장, NOT_PERMITTED인증안됨
    ROLE_USER, ROLE_NOT_PERMITTED_TEACHER, ROLE_TEACHER, ROLE_NOT_PERMITTED_DIRECTOR, ROLE_DIRECTOR, ROLE_ADMIN
}
