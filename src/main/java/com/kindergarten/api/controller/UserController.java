package com.kindergarten.api.controller;

import com.kindergarten.api.common.exception.CUserExistException;
import com.kindergarten.api.common.result.ListResult;
import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.model.request.SignUpRequest;
import com.kindergarten.api.repository.UserRepository;
import com.kindergarten.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@EnableSwagger2
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResponseService responseService;

    @GetMapping("/list")
    public ListResult<User> findAll() {

        return responseService.getListResult(userRepository.findAll());
    }

    @PostMapping("/parent")//회원가입

    public SingleResult<User> userSignUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        System.out.println("======================");
        System.out.println(signUpRequest.toString());
        System.out.println("======================");
        log.debug("REST request to signup USER : {}", signUpRequest.getUserid());
        if (userRepository.existsByUserid(signUpRequest.getUserid())) {
            throw new CUserExistException();
        }
        User user = User.builder()
                .userid(signUpRequest.getUserid())
                .name(signUpRequest.getName())
                .password(signUpRequest.getPassword())
                .email(signUpRequest.getEmail())
                .phone(signUpRequest.getPhone())
                .build();

        userService.signUpParent(user);

        return responseService.getSingleResult(user);
    }

    @PostMapping("/teacher")//회원가입
    public SingleResult<User> teacherSignup(@Valid @RequestBody SignUpRequest signUpRequest) {
        log.debug("REST request to signup TEACHER: {}", signUpRequest.getUserid());
        if (userRepository.existsByUserid(signUpRequest.getUserid())) {
            throw new CUserExistException();
        }
        User user = User.builder()
                .userid(signUpRequest.getUserid())
                .name(signUpRequest.getName())
                .password(signUpRequest.getPassword())
                .email(signUpRequest.getEmail())
                .phone(signUpRequest.getPhone())
                .build();

        userService.signUpTeacher(user);


        return responseService.getSingleResult(user);
    }

    @PostMapping("/director")//회원가입
    public SingleResult<User> directorSignup(@Valid @RequestBody SignUpRequest signUpRequest) {
        log.debug("REST request to signup DIRECTORz: {}", signUpRequest.getUserid());
        if (userRepository.existsByUserid(signUpRequest.getUserid())) {
            throw new CUserExistException();
        }
        User user = User.builder()
                .userid(signUpRequest.getUserid())
                .name(signUpRequest.getName())
                .password(signUpRequest.getPassword())
                .email(signUpRequest.getEmail())
                .phone(signUpRequest.getPhone())
                .build();

        return responseService.getSingleResult(user);
    }

}
