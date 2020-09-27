package com.kindergarten.api.service.Impl;

import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserid(userid);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(user.get().getUserid());
        }
        return new UserDetailsImpl(user.get());
    }
}
