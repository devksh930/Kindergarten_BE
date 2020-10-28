package com.kindergarten.api.controller;

import com.kindergarten.api.common.exception.CUserNotFoundException;
import com.kindergarten.api.common.result.ListResult;
import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.model.dto.UserDTO;
import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.repository.KinderGartenRepository;
import com.kindergarten.api.repository.UserRepository;
import com.kindergarten.api.security.util.JwtTokenProvider;
import com.kindergarten.api.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.util.List;

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
    private final JwtTokenProvider jwtTokenProvider;
    private final KinderGartenRepository kinderGartenRepository;

    public UserController(UserRepository userRepository, ResponseService responseService, UserService userService, ModelMapper modelMapper, JwtTokenProvider jwtTokenProvider, KinderGartenRepository kinderGartenRepository) {
        this.userRepository = userRepository;
        this.responseService = responseService;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.kinderGartenRepository = kinderGartenRepository;
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
    public SingleResult<UserDTO.Response> userSignUp(@Valid @RequestBody UserDTO.Create userdto) {

        log.debug("REST request to signup USER : {}", userdto.getUserid());
        User user = userService.registerAccount(userdto);
        UserDTO.Response response = modelMapper.map(user, UserDTO.Response.class);

        return responseService.getSingleResult(response);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @Transactional
    @PutMapping
    public String userModify(@RequestHeader("X-AUTH-TOKEN") HttpHeaders httpHeaders, @RequestBody UserDTO.UserModify userModify) {
        String s = httpHeaders.get("X-AUTH-TOKEN").get(0);
        String userId = jwtTokenProvider.getUserId(s);
        User user = userRepository.findByUserid(userId).orElseThrow(CUserNotFoundException::new);
        String ROLE = user.getRole().name();
        String phone = userModify.getPhone();
        String email = userModify.getEmail();
        String kinderid = userModify.getKindergraten_id();
        List<UserDTO.ADD_Student> student = userModify.getStudent();
        String kindergraten_id = userModify.getKindergraten_id();

        if (ROLE.equals("ROLE_NOT_PERMITTED_TEACHER")) {

            if (!phone.isBlank()) {
                user.setPhone(phone);
            }
            if (!email.isBlank()) {
                user.setEmail(email);
            }
            if (!kindergraten_id.isBlank()
            ) {
                user.setKinderGarten(kinderGartenRepository.findById(Long.valueOf((kindergraten_id))).get());
            }
            userRepository.save(user);

        }
        if (ROLE.equals("ROLE_TEACHER")) {
            log.debug("==========================");
            log.debug("ROLE_TEACHER");
            log.debug("==========================");

        }

        if (ROLE.equals("NOT_PERMITTED_DIRECTOR")) {
            log.debug("==========================");
            log.debug("NOT_PERMIT_DIRECTOR");
            log.debug("==========================");
        }
        if (ROLE.equals("ROLE_DIRECTOR")) {

            log.debug("==========================");
            log.debug("ROLE_DIRECTOR");
            log.debug("==========================");
        }
        if (ROLE.equals("ROLE_USER")) {
            log.debug("==========================");
            log.debug("ROLE_USER");
            log.debug("==========================");
        }
        return user.getPhone();
    }
}


