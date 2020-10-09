package com.kindergarten.api.service;

import com.kindergarten.api.common.exception.CUserExistException;
import com.kindergarten.api.common.exception.CUserIncorrectPasswordException;
import com.kindergarten.api.common.exception.CUserNotFoundException;
import com.kindergarten.api.model.entity.Student;
import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.model.entity.UserRole;
import com.kindergarten.api.model.dto.UserDTO;
import com.kindergarten.api.repository.KinderGartenRepository;
import com.kindergarten.api.repository.UserRepository;
import com.kindergarten.api.security.salt.Salt;
import com.kindergarten.api.security.util.RedisUtil;
import com.kindergarten.api.security.util.SaltUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SaltUtil saltUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private KinderGartenRepository kinderGartenRepository;

    @Autowired
    private StudentService studentService;

    public boolean isexistUser(String userid) {
        Optional<User> byUserid = userRepository.findByUserid(userid);
        byUserid.ifPresent(user -> {
            throw new CUserExistException();
        });
        return false;
    }

    @Transactional
    public User registerAccount(UserDTO.Create userdto) {
        userRepository.findByUserid(userdto.getUserid()).ifPresent(user -> {
            throw new CUserExistException();
        });
        User user = new User();
        if (userdto.getRole().equals("USER")) {
            user = this.signUpParent(userdto);

        }
        if (userdto.getRole().equals("TEACHER") || userdto.getRole().equals("DIRECTOR")) {
            user = this.signUpTeacher(userdto);
        }
        return user;
    }

    @Transactional
    public User signUpTeacher(UserDTO.Create userdto) {
        User newuser = new User();
        newuser.setUserid(userdto.getUserid());
        newuser.setRole(UserRole.ROLE_NOT_PERMITTED_TEACHER);

        if (userdto.getRole().equals("DIRECTOR")) {
            newuser.setRole(UserRole.ROLE_NOT_PERMITTED_DIRECTOR);
        }
        newuser.setPhone(userdto.getPhone());
        newuser.setKinderGarten(kinderGartenRepository.findById(Long.valueOf(userdto.getKindergarten_id())).get());
        newuser.setName(userdto.getName());
        newuser.setEmail(userdto.getEmail());
        String salt = saltUtil.genSalt();
        newuser.setSalt(new Salt(salt));
        newuser.setPassword(saltUtil.encodedPassword(salt, userdto.getPassword()));
        userRepository.save(newuser);

        return newuser;
    }

    @Transactional
    public User signUpParent(UserDTO.Create userdto) {

        User newuser = new User();
        newuser.setUserid(userdto.getUserid());
        newuser.setRole(UserRole.ROLE_USER);
        newuser.setPhone(userdto.getPhone());
        newuser.setName(userdto.getName());
        newuser.setEmail(userdto.getEmail());

        String salt = saltUtil.genSalt();
        newuser.setSalt(new Salt(salt));

        newuser.setPassword(saltUtil.encodedPassword(salt, userdto.getPassword()));

        userRepository.save(newuser);

        if (!userdto.getStudent().isEmpty()) {
            List<UserDTO.ADD_Student> students = userdto.getStudent();

            List<Student> newstudent = studentService.addStudent(students, newuser);
            newuser.setStudent(newstudent);
        }

        return newuser;
    }


    public boolean isPasswordUuidValidate(String key) {
        String userId = redisUtil.getData(key);
        if (userId.equals("")) {
            return false;
        }
        return true;
    }


    @Transactional
    public User loginUser(String userid, String password) {

        Optional<User> user = userRepository.findByUserid(userid);

        if (user.isEmpty()) {
            throw new CUserNotFoundException();
        }
        String salt = user.get().getSalt().getSalt();
        password = saltUtil.encodedPassword(salt, password);


        if (!user.get().getPassword().equals(password)) {
            throw new CUserIncorrectPasswordException();
        }
        return user.get();
    }
}
