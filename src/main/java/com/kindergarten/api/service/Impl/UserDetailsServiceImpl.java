package com.kindergarten.api.service.Impl;

import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        User user = userRepository.findByUserid(userid);
        if (user==null) {
            throw new UsernameNotFoundException(user.getUserid());
        }
        return new UserDetailsImpl(user);
    }
}
