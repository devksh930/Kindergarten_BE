package com.kindergarten.api.controller;

import com.kindergarten.api.common.exception.CUserExistException;
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
import org.springframework.context.annotation.Role;
import org.springframework.security.access.AccessDeniedException;
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


    public UserController(ModelMapper modelMapper, UserService userService, UserRepository userRepository, ResponseService responseService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.userRepository = userRepository;
        this.responseService = responseService;

    }

    @GetMapping("/list")
    public ListResult<User> findAll() {

        return responseService.getListResult(userRepository.findAll());
    }

    @GetMapping("/existid/{userid}")
    public SingleResult existuserId(@PathVariable String userid) {

        String msg = null;
        boolean isexistUser = userService.isexistUser(userid);
        if (!isexistUser) {
            msg = "존재하지 않는 아이디 입니다";
        }
        return responseService.getSingleResult(msg);
    }

    @PostMapping//회원가입
    public SingleResult<UserDTO.Response> userSignUp(@Valid @RequestBody UserDTO.Create userdto) {
        log.debug("REST request to signup USER : {}", userdto.getUserid());

        User user = userService.registerAccount(userdto);
        UserDTO.Response response = modelMapper.map(user, UserDTO.Response.class);

        return responseService.getSingleResult(response);
    }


}
