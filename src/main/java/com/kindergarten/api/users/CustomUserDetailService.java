package com.kindergarten.api.users;

import com.kindergarten.api.common.exception.CUserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor

public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String userid) {
        CustomUserDetail userDetails = null;
        User user = userRepository.findByUserid(userid).orElseThrow(CUserNotFoundException::new);
        userDetails = new CustomUserDetail(user);
        return userDetails;
    }
}
