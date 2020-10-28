package com.kindergarten.api.controller;

import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.model.dto.UserDTO;
import com.kindergarten.api.repository.UserRepository;
import com.kindergarten.api.security.util.JwtTokenProvider;
import com.kindergarten.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@RequestMapping("/api/auth")
@EnableSwagger2
@Slf4j
//
@CrossOrigin("*")
public class AuthController {
    private final ResponseService responseService;

    private final UserService userService;


    private final JwtTokenProvider jwtUtil;


    private final UserRepository userRepository;

    public AuthController(ResponseService responseService, UserService userService, JwtTokenProvider jwtUtil, UserRepository userRepository) {
        this.responseService = responseService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public SingleResult<String> loginUser(@RequestBody UserDTO.Login login) {
        String s = userService.loginUser(login.getUserid(), login.getPassword());
        return responseService.getSingleResult(s);
    }


}
