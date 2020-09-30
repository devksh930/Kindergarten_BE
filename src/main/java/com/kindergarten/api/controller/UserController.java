package com.kindergarten.api.controller;

import com.kindergarten.api.common.exception.CUserExistException;
import com.kindergarten.api.common.reqeust.RequestLoginUser;
import com.kindergarten.api.common.result.ListResult;
import com.kindergarten.api.common.response.LoginResponse;
import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.model.request.SignUpRequest;
import com.kindergarten.api.repository.UserRepository;
import com.kindergarten.api.security.util.CookieUtil;
import com.kindergarten.api.security.util.JwtUtil;
import com.kindergarten.api.security.util.RedisUtil;
import com.kindergarten.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@EnableSwagger2
@Slf4j
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResponseService responseService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CookieUtil cookieUtil;
    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/list")
    public ListResult<User> findAll() {

        return responseService.getListResult(userRepository.findAll());
    }

    @GetMapping("/existid/{userid}")
    public SingleResult existuserId(@PathVariable String userid) {
        log.debug("==============================================");
        log.debug(userid);
        log.debug("==============================================");

        SingleResult singleResult;

        singleResult = userService.existUserId(userid);

        return singleResult;
    }

    @PostMapping("/parent")//회원가입
    public SingleResult<User> userSignUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        log.debug("REST request to signup USER : {}", signUpRequest.getUserid());

        User user = modelMapper.map(signUpRequest, User.class);

        userService.signUpParent(user);

        return responseService.getSingleResult(user);
    }

    @PostMapping("/teacher")//회원가입
    public SingleResult<User> teacherSignup(@Valid @RequestBody SignUpRequest signUpRequest) {
        log.debug("REST request to signup TEACHER: {}", signUpRequest.getUserid());

        User user = modelMapper.map(signUpRequest, User.class);

        userService.signUpParent(user);


        return responseService.getSingleResult(user);
    }

    @PostMapping("/director")//회원가입
    public SingleResult<User> directorSignup(@Valid @RequestBody SignUpRequest signUpRequest) {
        log.debug("REST request to signup DIRECTOR: {}", signUpRequest.getUserid());

        User user = modelMapper.map(signUpRequest, User.class);

        userService.signUpParent(user);

        return responseService.getSingleResult(user);
    }

    @PostMapping("/login")//로그인
    public LoginResponse login(@RequestBody RequestLoginUser user,
                               HttpServletRequest request, HttpServletResponse response) {
        try {
            final User loginUser = userService.loginUser(user.getUserid(), user.getPassword());
            final String token = jwtUtil.generateToken(loginUser);
            final String refreshJwt = jwtUtil.generateRefreshToken(loginUser);
            Cookie accessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME, token);
            Cookie refreshToken = cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, refreshJwt);
            redisUtil.setDataExpire(refreshJwt, user.getUserid(), JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
            response.addCookie(accessToken);
            response.addCookie(refreshToken);
            return new LoginResponse("success", "로그인성공", token);
        } catch (Exception e) {
            e.printStackTrace();
            return new LoginResponse("error", "실패", e.getMessage());
        }
    }

}
