package com.kindergarten.api.controller;

<<<<<<< HEAD
import com.kindergarten.api.model.request.RequestLoginUser;
=======
>>>>>>> develop
import com.kindergarten.api.common.response.LoginResponse;
import com.kindergarten.api.common.result.ListResult;
import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.model.dto.UserDTO;
import com.kindergarten.api.repository.KinderGartenRepository;
import com.kindergarten.api.repository.StudentRepository;
import com.kindergarten.api.repository.UserRepository;
import com.kindergarten.api.security.util.CookieUtil;
import com.kindergarten.api.security.util.JwtUtil;
import com.kindergarten.api.security.util.RedisUtil;
import com.kindergarten.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
public class UserController {

    private final ModelMapper modelMapper;

    private final UserService userService;

    private final UserRepository userRepository;

    private final ResponseService responseService;

    private final JwtUtil jwtUtil;

    private final CookieUtil cookieUtil;

    private final RedisUtil redisUtil;

    private final StudentRepository studentRepository;

    private final KinderGartenRepository kinderGartenRepository;

    public UserController(ModelMapper modelMapper, UserService userService, UserRepository userRepository, ResponseService responseService, JwtUtil jwtUtil, CookieUtil cookieUtil, RedisUtil redisUtil, StudentRepository studentRepository, KinderGartenRepository kinderGartenRepository) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.userRepository = userRepository;
        this.responseService = responseService;
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
        this.redisUtil = redisUtil;
        this.studentRepository = studentRepository;
        this.kinderGartenRepository = kinderGartenRepository;
    }

    @GetMapping("/list")
    public ListResult<User> findAll() {

        return responseService.getListResult(userRepository.findAll());
    }

//    @GetMapping("/existid/{userid}")
//    public SingleResult existuserId(@PathVariable String userid) {
//        log.debug("==============================================");
//        log.debug(userid);
//        log.debug("==============================================");
//
//        SingleResult singleResult;
//
//        singleResult = userService.existUserId(userid);
//
//        return singleResult;
//    }

    @PostMapping//회원가입
    public SingleResult<UserDTO.Response> userSignUp(@Valid @RequestBody UserDTO.Create userdto) {
        log.debug("REST request to signup USER : {}", userdto.getUserid());

        User user = userService.registerAccount(userdto);
        UserDTO.Response response = modelMapper.map(user, UserDTO.Response.class);

        return responseService.getSingleResult(response);
    }


    @PostMapping("/login")//로그인
    public LoginResponse login(@RequestBody UserDTO.Login login,
                               HttpServletRequest request, HttpServletResponse response) {
        try {

            final User loginUser = userService.loginUser(login.getUserid(), login.getPassword());
            final String token = jwtUtil.generateToken(loginUser);
            final String refreshJwt = jwtUtil.generateRefreshToken(loginUser);

            Cookie accessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME, token);
            Cookie refreshToken = cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, refreshJwt);

            redisUtil.setDataExpire(refreshJwt, login.getUserid(), JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);

            response.addCookie(accessToken);
            response.addCookie(refreshToken);


            return new LoginResponse("success", "로그인성공", token);
        } catch (Exception e) {
            e.printStackTrace();
            return new LoginResponse("error", "실패", e.getMessage());

        }
    }
}
