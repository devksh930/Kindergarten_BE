package com.kindergarten.api.security.service;

import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.model.entity.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class CustomUserDetail extends org.springframework.security.core.userdetails.User {

    public CustomUserDetail(User user) {
        super(user.getUserid(), user.getPassword(), authorities(user));
    }

    private static Collection<? extends GrantedAuthority> authorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getRole().equals(UserRole.ROLE_ADMIN)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_ADMIN.name()));
        }
        if (user.getRole().equals(UserRole.ROLE_DIRECTOR)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_DIRECTOR.name()));
        }
        if (user.getRole().equals(UserRole.ROLE_NOT_PERMITTED_DIRECTOR)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_NOT_PERMITTED_DIRECTOR.name()));
        }
        if (user.getRole().equals(UserRole.ROLE_NOT_PERMITTED_TEACHER)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_NOT_PERMITTED_TEACHER.name()));
        }
        if (user.getRole().equals(UserRole.ROLE_USER)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_USER.name()));
        }
        if (user.getRole().equals(UserRole.ROLE_TEACHER)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_TEACHER.name()));
        }
        if (user.getRole().equals(UserRole.ROLE_DIRECTOR)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_DIRECTOR.name()));
        }
        if (user.getRole().equals(UserRole.ROLE_ADMIN)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_ADMIN.name()));
        }
        return authorities;
    }

}
