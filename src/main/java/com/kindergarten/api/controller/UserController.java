package com.kindergarten.api.controller;

import com.kindergarten.api.common.result.CommonResult;
import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.model.dto.UserDTO;
import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.repository.UserRepository;
import com.kindergarten.api.security.util.JwtTokenProvider;
import com.kindergarten.api.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/api/users")
@EnableSwagger2
@Slf4j
//@CrossOrigin(origins = "https://mommyogi.com")
@CrossOrigin("*")

public class UserController {
    private final ResponseService responseService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public UserController(ResponseService responseService, UserService userService, ModelMapper modelMapper, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.responseService = responseService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회한다")
    @GetMapping("/list")
    public SingleResult findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String name = authentication.getName();
        return responseService.getSingleResult(name);
    }

    @GetMapping("/existid/{userid}")//GET:/api/users/existid/{@PathVariable}
    public SingleResult existuserId(@PathVariable String userid) {
        log.debug("REST request to exist USER : {}", userid);

        String msg = null;
        boolean isexistUser = userService.isexistsByUserid(userid);
        if (!isexistUser) {
            msg = "사용가능한 아이디입니다.";
        }
        return responseService.getSingleResult(msg);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping//회원가입
    public SingleResult<UserDTO.Response> userSignUp(@Valid @RequestBody UserDTO.Create userdto) {

        log.debug("REST request to signup USER : {}", userdto.getUserid());

        User user = userService.registerAccount(userdto);
        UserDTO.Response response = modelMapper.map(user, UserDTO.Response.class);

        return responseService.getSingleResult(response);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PutMapping
    public CommonResult userModify(@RequestBody UserDTO.UserModify userModify) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String ROLE = authorities.toString();

//        미인증 선생, 선생
        if (ROLE.equals("[ROLE_NOT_PERMITTED_TEACHER]") || ROLE.equals("[NOT_PERMITTED_DIRECTOR]")) {
            userService.modifyUser(authentication, userModify);
        }
//        인증된 선생, 원장
        if (ROLE.equals("[ROLE_TEACHER]") || ROLE.equals("[ROLE_DIRECTOR]")) {
            userModify.setKindergraten_id(null);
            userService.modifyUser(authentication, userModify);
        }
//        회원
        if (ROLE.equals("[ROLE_USER]")) {
            userModify.setKindergraten_id(null);
            userService.modifyUser(authentication, userModify);
        }
        return responseService.getSuccessResult();
    }
}


