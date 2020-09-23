package com.kindergarten.api.service.Impl;

import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.model.entity.UserRole;
import com.kindergarten.api.repository.UserRepository;
import com.kindergarten.api.security.entitiy.Salt;
import com.kindergarten.api.security.SaltUtil;
import com.kindergarten.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SaltUtil saltUtil;


    @Override
    @Transactional
    public void signUpParent(User user) {

        user.setRole(UserRole.ROLE_USER);
        String password = user.getPassword();
        String salt = saltUtil.genSalt();
        user.setSalt(new Salt(salt));
        user.setPassword(saltUtil.encodedPassword(salt, password));
        userRepository.save(user);
        log.debug("학부모 회원가입");
    }

    @Override
    @Transactional
    public void signUpTeacher(User user) {
        user.setRole(UserRole.ROLE_NOT_PERMITTED_TEACHER);
        String password = user.getPassword();
        String salt = saltUtil.genSalt();
        user.setSalt(new Salt(salt));
        user.setPassword(saltUtil.encodedPassword(salt, password));

        userRepository.save(user);
        log.debug("선생님 회원가입");
    }


    @Override
    @Transactional
    public void signUpDirector(User user) {

        user.setRole(UserRole.ROLE_NOT_PERMITTED_DIRECTOR);
        String password = user.getPassword();
        String salt = saltUtil.genSalt();
        user.setSalt(new Salt(salt));
        user.setPassword(saltUtil.encodedPassword(salt, password));

        userRepository.save(user);
        log.debug("원장님 회원가입");

    }


    @Override
    @Transactional
    public User loginUser(String userid, String password) throws Exception {
        Optional<User> user = userRepository.findByUserid(userid);
        if (user.isEmpty()) throw new Exception("유저가 존재하지 않습니다");
        String salt = user.get().getSalt().getSalt();
        password = saltUtil.encodedPassword(salt, password);
        if (!user.get().getPassword().equals(password)) {
            throw new Exception("비밀번호가 틀립니다");
        }
        return user.get();
    }
}
