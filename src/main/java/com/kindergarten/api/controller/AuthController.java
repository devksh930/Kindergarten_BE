package com.kindergarten.api.controller;

import com.kindergarten.api.common.result.CommonResult;
import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.users.UserDTO;
import com.kindergarten.api.users.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@RequestMapping("/api/auth")
@EnableSwagger2
@RequiredArgsConstructor
@Slf4j
@CrossOrigin("*")
public class AuthController {
    private final ResponseService responseService;

    private final UserService userService;


    @PostMapping("/login")
    public SingleResult<UserDTO.Login_response> loginUser(@RequestBody UserDTO.Login login) {
        UserDTO.Login_response response = userService.loginUser(login.getUserid(), login.getPassword());
        return responseService.getSingleResult(response);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/currentuser")
    public SingleResult<UserDTO.currentUser> currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDTO.currentUser currentUser = userService.currentUser(authentication.getName());

        return responseService.getSingleResult(currentUser);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/passwordvalid")
    public CommonResult passwordVaild(@RequestBody UserDTO.userPasswordValid pasword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Boolean passwordValid = userService.passwordValid(authentication.getName(), pasword.getPassword());
        return responseService.getSingleResult(passwordValid);
    }
}
