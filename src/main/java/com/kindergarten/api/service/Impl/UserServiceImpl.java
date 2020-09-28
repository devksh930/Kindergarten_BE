package com.kindergarten.api.service.Impl;

import com.kindergarten.api.common.exception.CUserExistException;
import com.kindergarten.api.common.exception.CUserIncorrectPasswordException;
import com.kindergarten.api.common.exception.CUserNotFoundException;
import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.model.entity.UserRole;
import com.kindergarten.api.repository.UserRepository;
import com.kindergarten.api.security.entitiy.Salt;
import com.kindergarten.api.security.util.SaltUtil;
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
        Boolean existsByUserid = userRepository.existsByUserid(user.getUserid());
        if (!existsByUserid) {
            user.setRole(UserRole.ROLE_USER);
            String password = user.getPassword();
            String salt = saltUtil.genSalt();
            user.setSalt(new Salt(salt));
            user.setPassword(saltUtil.encodedPassword(salt, password));
            userRepository.save(user);
            log.debug("학부모 회원가입 성공");

        } else {
            log.debug("학부모 회원가입 실패");
            throw new CUserExistException();
        }

    }

    @Override
    @Transactional
    public void signUpTeacher(User user) {
        Boolean existsByUserid = userRepository.existsByUserid(user.getUserid());
        if (!existsByUserid) {

            user.setRole(UserRole.ROLE_NOT_PERMITTED_TEACHER);
            String password = user.getPassword();
            String salt = saltUtil.genSalt();
            user.setSalt(new Salt(salt));
            user.setPassword(saltUtil.encodedPassword(salt, password));

            userRepository.save(user);
            log.debug("선생님 회원가입");
        } else {
            log.debug("선생님 회원가입 실패");
            throw new CUserExistException();

        }
    }


    @Override
    @Transactional
    public void signUpDirector(User user) {
        Boolean existsByUserid = userRepository.existsByUserid(user.getUserid());
        if (!existsByUserid) {
            user.setRole(UserRole.ROLE_NOT_PERMITTED_DIRECTOR);
            String password = user.getPassword();
            String salt = saltUtil.genSalt();
            user.setSalt(new Salt(salt));
            user.setPassword(saltUtil.encodedPassword(salt, password));

            userRepository.save(user);

            log.debug("원장님 회원가입");
        } else {
            log.debug("원장님 회원가입 실패");
            throw new CUserExistException();

        }

    }

    @Override
    public SingleResult existUserId(String userid) {
        Boolean existsByUserid = userRepository.existsByUserid(userid);
        ResponseService responseService = new ResponseService();
        SingleResult<Object> singleResult;
        if (existsByUserid) {
            singleResult = responseService.getSingleResult("중복되지않은 아이디 입니다.");
        } else {
            throw new CUserExistException();
        }
        return singleResult;

    }


    @Override
    @Transactional
    public User loginUser(String userid, String password) throws Exception {

        Optional<User> user = userRepository.findByUserid(userid);

        if (user.isEmpty()) throw new CUserNotFoundException();
        String salt = user.get().getSalt().getSalt();
        password = saltUtil.encodedPassword(salt, password);

        if (!user.get().getPassword().equals(password)) {
            throw new CUserIncorrectPasswordException();
        }
        return user.get();
    }
}
