package com.kindergarten.api.controller;

import com.kindergarten.api.common.exception.CUserNotFoundException;
import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.users.UserDTO;
import com.kindergarten.api.users.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collection;

@RestController
@RequestMapping("/api/auth")
@EnableSwagger2
@Slf4j
//
@CrossOrigin("*")
public class AuthController {
    private final ResponseService responseService;

    private final UserService userService;


    public AuthController(ResponseService responseService, UserService userService) {
        this.responseService = responseService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public SingleResult<UserDTO.Login_response> loginUser(@RequestBody UserDTO.Login login) {
        UserDTO.Login_response response = userService.loginUser(login.getUserid(), login.getPassword());
        return responseService.getSingleResult(response);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/currentuser")
    public SingleResult<String> currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String ROLE = authorities.toString().replace("[", "").replace("]", "");

        if (authorities == null || ROLE.equals("ROLE_ANONYMOUS")) {
            throw new CUserNotFoundException();
        }
        return responseService.getSingleResult(ROLE);
    }
}
