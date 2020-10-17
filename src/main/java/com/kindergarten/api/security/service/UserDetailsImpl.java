package com.kindergarten.api.security.service;

import com.kindergarten.api.model.entity.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsImpl extends User {

    public UserDetailsImpl(com.kindergarten.api.model.entity.User user) {
        super(user.getUserid(), user.getPassword(), authorities(user));
    }


    private static Collection<? extends GrantedAuthority> authorities(com.kindergarten.api.model.entity.User user) {
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
