package com.kindergarten.api.controller;

import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.model.dto.UserDTO;
import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.repository.UserRepository;
import com.kindergarten.api.security.util.CookieUtil;
import com.kindergarten.api.security.util.JwtUtil;
import com.kindergarten.api.security.util.RedisUtil;
import com.kindergarten.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
@EnableSwagger2
@Slf4j
@CrossOrigin(origins = "https://mommyogi.com")
public class AuthController {
    private final ResponseService responseService;

    private final UserService userService;

    private final CookieUtil cookieUtil;

    private final JwtUtil jwtUtil;

    private final RedisUtil redisUtil;

    private final UserRepository userRepository;

    public AuthController(ResponseService responseService, UserService userService, CookieUtil cookieUtil, JwtUtil jwtUtil, RedisUtil redisUtil, UserRepository userRepository) {
        this.responseService = responseService;
        this.userService = userService;
        this.cookieUtil = cookieUtil;
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/currentuser")
    public String getCurrentUser(HttpServletRequest request, HttpServletResponse response) {
        Cookie refreshToken = cookieUtil.getCookie(request, "refreshToken");
//        Cookie accessToken = cookieUtil.getCookie(request, "accessToken");
        String userid = jwtUtil.getUserid(refreshToken.getValue());
        return userid + "님" + " 안녕하세요!";
    }

    @PostMapping("/login")//로그인
    public SingleResult login(@RequestBody UserDTO.Login login,
                              HttpServletRequest request, HttpServletResponse response) {

        final User loginUser = userService.loginUser(login.getUserid(), login.getPassword());
        final String token = jwtUtil.generateToken(loginUser);
        final String refreshJwt = jwtUtil.generateRefreshToken(loginUser);

        Cookie accessToken = cookieUtil.createCookie(JwtUtil.ACCESS_TOKEN_NAME, token);
        Cookie refreshToken = cookieUtil.createCookie(JwtUtil.REFRESH_TOKEN_NAME, refreshJwt);

        redisUtil.setDataExpire(refreshJwt, login.getUserid(), JwtUtil.REFRESH_TOKEN_VALIDATION_SECOND);
        response.addCookie(accessToken);
        response.addCookie(refreshToken);

        String accessToken1 = cookieUtil.getCookie(request, "accessToken").getValue();

        String userid = jwtUtil.getUserid(accessToken1);
        return responseService.getSingleResult(userid);

    }

    @GetMapping("/logout")
    public SingleResult logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie refreshToken = cookieUtil.getCookie(request, "refreshToken");
        String loginUserId = redisUtil.getData(refreshToken.getValue());
        redisUtil.deleteData(refreshToken.getValue());

        Cookie expiredaccessToken = new Cookie("refreshToken", "");
        expiredaccessToken.setPath("/");
        expiredaccessToken.setHttpOnly(true);

        Cookie expiredrefreshToken = new Cookie("accessToken", "");
        expiredrefreshToken.setPath("/");
        expiredrefreshToken.setHttpOnly(true);

        response.addCookie(expiredaccessToken);
        response.addCookie(expiredrefreshToken);
        return responseService.getSingleResult(loginUserId + "  로그아웃");
    }

}
