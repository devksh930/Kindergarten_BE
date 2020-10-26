package com.kindergarten.api.controller;

import com.kindergarten.api.common.result.CommonResult;
import com.kindergarten.api.common.result.ListResult;
import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.model.dto.UserDTO;
import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.repository.UserRepository;
import com.kindergarten.api.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@EnableSwagger2
@Slf4j
//@CrossOrigin(origins = "https://mommyogi.com")
@CrossOrigin("*")

public class UserController {
    private final UserRepository userRepository;
    private final ResponseService responseService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserRepository userRepository, ResponseService responseService, UserService userService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.responseService = responseService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "회원 리스트 조회", notes = "모든 회원을 조회한다")
    @GetMapping("/list")
    public ListResult<User> findAll() {
        return responseService.getListResult(userRepository.findAll());
    }

    @GetMapping("/existid/{userid}")//GET:/api/users/existid/{@PathVariable}
    public SingleResult existuserId(@PathVariable String userid) {

        String msg = null;
        boolean isexistUser = userService.isexistsByUserid(userid);
        if (!isexistUser) {
            msg = "사용가능한 아이디입니다.";
        }
        return responseService.getSingleResult(msg);
    }

    @ResponseStatus(value = HttpStatus.CREATED)
    @PostMapping//회원가입
    public CommonResult userSignUp(@Valid @RequestBody UserDTO.Create userdto) {
        log.debug("REST request to signup USER : {}", userdto.getUserid());
        User user = userService.registerAccount(userdto);
        UserDTO.Response response = modelMapper.map(user, UserDTO.Response.class);

        return responseService.getSuccessResult();
    }


}
