package com.kindergarten.api.service;

import com.kindergarten.api.common.exception.CUserExistException;
import com.kindergarten.api.common.exception.CUserIncorrectPasswordException;
import com.kindergarten.api.common.exception.CUserNotFoundException;
import com.kindergarten.api.model.dto.UserDTO;
import com.kindergarten.api.model.entity.Student;
import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.model.entity.UserRole;
import com.kindergarten.api.repository.KinderGartenRepository;
import com.kindergarten.api.repository.UserRepository;
import com.kindergarten.api.security.util.JwtTokenProvider;
import com.kindergarten.api.security.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;


    private final RedisUtil redisUtil;
    private final KinderGartenRepository kinderGartenRepository;
    private final StudentService studentService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, RedisUtil redisUtil, KinderGartenRepository kinderGartenRepository, StudentService studentService, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.redisUtil = redisUtil;
        this.kinderGartenRepository = kinderGartenRepository;
        this.studentService = studentService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public boolean isexistsByUserid(String userid) {
        Boolean byUserid = userRepository.existsByUserid(userid);
        if (byUserid) {
            throw new CUserExistException();
        }
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
        newuser.setPassword(passwordEncoder.encode(userdto.getPassword()));
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
        newuser.setPassword(passwordEncoder.encode(userdto.getPassword()));


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

    public User findUserid(String userid) {
        Optional<User> byId = userRepository.findByUserid(userid);

        return byId.get();
    }

    @Transactional
    public String loginUser(String userid, String password) {

        User user = userRepository.findByUserid(userid).orElseThrow(CUserNotFoundException::new);


        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CUserIncorrectPasswordException();
        }
        return jwtTokenProvider.createToken(String.valueOf(user.getUserid()), user.getRole().name());
    }
}
