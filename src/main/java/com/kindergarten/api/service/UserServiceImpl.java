package com.kindergarten.api.service;

import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.model.entity.UserRole;
import com.kindergarten.api.repository.UserRepository;
import com.kindergarten.api.security.entitiy.Salt;
import com.kindergarten.api.security.repository.SaltRepository;
import com.kindergarten.api.security.service.SaltUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.lang.reflect.Member;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SaltRepository saltRepository;
    @Autowired
    private SaltUtil saltUtil;


    @Override
    @Transactional
    public void signUpParent(User user) {
        user.setRole(UserRole.ROLE_USER);
        String password = user.getPassword();
        String salt = saltUtil.genSalt();
        log.info(salt);
        user.setSalt(new Salt(salt));
        user.setPassword(saltUtil.encodedPassword(salt, password));
        userRepository.save(user);
    }

    @Override
    public void signUpTeacher(User user) {

    }

    @Override
    public void signUpDirector(User user) {

    }

    @Override
    public User loginUser(String userid, String password) throws Exception {
        User user = userRepository.findByUserid(userid);
        if (user == null) throw new Exception("유저가 존재하지 않습니다");
        String salt = user.getSalt().getSalt();
        password = saltUtil.encodedPassword(salt, password);
        if (!user.getPassword().equals(password)) {
            throw new Exception("비밀번호가 틀립니다");
        }
        return user;
    }
}
