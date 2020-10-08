package com.kindergarten.api.security.service;

import com.kindergarten.api.common.exception.CUserNotFoundException;
import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserid(userid);
        if (user.isEmpty()) {
            throw new CUserNotFoundException();
        }
        return new UserDetailsImpl(user.get());
    }
}
