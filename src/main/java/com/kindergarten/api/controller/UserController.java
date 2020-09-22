package com.kindergarten.api.controller;

import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.model.request.SignUpRequest;
import com.kindergarten.api.repository.UserRepository;
import com.kindergarten.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@EnableSwagger2
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/parent")//회원가입
    public ResponseEntity<?> userSignUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        log.debug("REST request to signup : {}", signUpRequest.getUserid());
        if (userRepository.existsByUserid(signUpRequest.getUserid())) {
            throw new RuntimeException("이미 존재하는 id 입니다.");
        }
        User user = User.builder()
                .userid(signUpRequest.getUserid())
                .name(signUpRequest.getName())
                .password(signUpRequest.getPassword())
                .email(signUpRequest.getEmail())
                .phone(signUpRequest.getPhone())
                .build();

        userService.signUpParent(user);

        Map<String, Object> msg = new HashMap<>();
        msg.put("user_id", user.getUserid());
        msg.put("msg", "회원가입에 성공했습니다.");

        return new ResponseEntity(msg, HttpStatus.CREATED);
    }

    @PostMapping("/teacher")//회원가입
    public ResponseEntity<?> teacherSignup(@Valid @RequestBody SignUpRequest signUpRequest) {
        log.debug("REST request to signup : {}", signUpRequest.getUserid());
        if (userRepository.existsByUserid(signUpRequest.getUserid())) {
            throw new RuntimeException("이미 존재하는 id 입니다.");
        }
        User user = User.builder()
                .userid(signUpRequest.getUserid())
                .name(signUpRequest.getName())
                .password(signUpRequest.getPassword())
                .email(signUpRequest.getEmail())
                .phone(signUpRequest.getPhone())
                .build();
        userService.signUpTeacher(user);

        Map<String, Object> msg = new HashMap<>();
        msg.put("user_id", user.getUserid());
        msg.put("msg", "회원가입에 성공했습니다.");

        return new ResponseEntity(msg, HttpStatus.CREATED);
    }

    @PostMapping("/director")//회원가입
    public ResponseEntity<?> directorSignup(@Valid @RequestBody SignUpRequest signUpRequest) {
        log.debug("REST request to signup : {}", signUpRequest.getUserid());
        if (userRepository.existsByUserid(signUpRequest.getUserid())) {
            throw new RuntimeException("이미 존재하는 id 입니다.");
        }
        User user = User.builder()
                .userid(signUpRequest.getUserid())
                .name(signUpRequest.getName())
                .password(signUpRequest.getPassword())
                .email(signUpRequest.getEmail())
                .phone(signUpRequest.getPhone())
                .build();

        Map<String, Object> msg = new HashMap<>();
        msg.put("user_id", user.getUserid());
        msg.put("msg", "회원가입에 성공했습니다.");

        return new ResponseEntity(msg, HttpStatus.CREATED);
    }

}
