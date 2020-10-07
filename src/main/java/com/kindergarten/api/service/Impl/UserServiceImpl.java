package com.kindergarten.api.service.Impl;

import com.kindergarten.api.common.exception.CUserExistException;
import com.kindergarten.api.common.exception.CUserIncorrectPasswordException;
import com.kindergarten.api.common.exception.CUserNotFoundException;
import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.model.entity.UserRole;
import com.kindergarten.api.repository.UserRepository;
import com.kindergarten.api.security.salt.Salt;
import com.kindergarten.api.security.util.RedisUtil;
import com.kindergarten.api.security.util.SaltUtil;
import com.kindergarten.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SaltUtil saltUtil;

    @Autowired
    private RedisUtil redisUtil;

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
    public boolean isPasswordUuidValidate(String key) {
        String userId = redisUtil.getData(key);
        if (userId.equals("")) {
            return false;
        }
        return true;
    }


    @Override
    public SingleResult existUserId(String userid) {
        Boolean existsByUserid = userRepository.existsByUserid(userid);
        ResponseService responseService = new ResponseService();
        SingleResult<Object> singleResult;
        if (!existsByUserid) {
            singleResult = responseService.getSingleResult("중복되지않은 아이디 입니다.");
        } else {
            throw new CUserExistException();
        }
        return singleResult;

    }


    @Override
    @Transactional
    public User loginUser(String userid, String password) throws Exception {

        User user = userRepository.findByUserid(userid);

        if (user == null) throw new CUserNotFoundException();
        String salt = user.getSalt().getSalt();
        password = saltUtil.encodedPassword(salt, password);


        if (!user.getPassword().equals(password)) {
            throw new CUserIncorrectPasswordException();
        }
        return user;
    }
}
