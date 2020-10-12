package com.kindergarten.api.controller;

import com.kindergarten.api.common.result.ListResult;
import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.model.dto.UserDTO;
import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.repository.UserRepository;
import com.kindergarten.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;

@CrossOrigin(origins = {"http://localhost:3000"})
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

    @GetMapping("/existid/{userid}")//GET:/api/users/existid/{@PathVariable}
    public SingleResult existuserId(@PathVariable String userid) {

        String msg = null;
        boolean isexistUser = userService.isexistsByUserid(userid);
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
