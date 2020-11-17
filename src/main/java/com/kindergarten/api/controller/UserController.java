package com.kindergarten.api.controller;

import com.kindergarten.api.common.exception.CNotOwnerException;
import com.kindergarten.api.common.exception.CUserNotFoundException;
import com.kindergarten.api.common.result.CommonResult;
import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.student.Student;
import com.kindergarten.api.student.StudentService;
import com.kindergarten.api.users.User;
import com.kindergarten.api.users.UserDTO;
import com.kindergarten.api.users.UserRepository;
import com.kindergarten.api.users.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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
import java.util.List;

@RestController
@RequestMapping("/api/users")
@EnableSwagger2
@Slf4j
//@CrossOrigin(origins = "https://mommyogi.com")
@CrossOrigin("*")
@RequiredArgsConstructor
public class UserController {
    private final ResponseService responseService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final StudentService studentService;


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

    @ApiOperation(value = "중복회원 조회", notes = "중복된 id가 있는지 검사한다")
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

    @ApiOperation(value = "회원 가입", notes = "회원 가입을 한다")
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
    @ApiOperation(value = "회원 정보 수정", notes = "회원정보를 수정한다.")
    @PutMapping
    public CommonResult userModify(@RequestBody UserDTO.UserModify userModify) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String ROLE = authorities.toString();

//        미인증 선생, 선생
        if (ROLE.equals("[ROLE_NOT_PERMITTED_TEACHER]") || ROLE.equals("[NOT_PERMITTED_DIRECTOR]")) {
            userService.modifyUser(authentication.getName(), userModify);
        }
//        인증된 선생, 원장
        if (ROLE.equals("[ROLE_TEACHER]") || ROLE.equals("[ROLE_DIRECTOR]")) {
            userModify.setKindergraten_id(null);
            userService.modifyUser(authentication.getName(), userModify);
        }
//        회원
        if (ROLE.equals("[ROLE_USER]")) {
            userModify.setKindergraten_id(null);
            userService.modifyUser(authentication.getName(), userModify);
        }
        return responseService.getSuccessResult();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/students")
    public SingleResult<UserDTO.Response_User_Student> userStudentList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO.Response_User_Student response_user_student = userService.parentStudents(authentication.getName());
        return responseService.getSingleResult(response_user_student);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/students")
    public CommonResult addStudent(@RequestBody UserDTO.ADD_Students add_students) {
//        로그인 유저 권한검사
        if (add_students.getStudents().isEmpty()) {
            throw new CNotOwnerException();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userid = authentication.getName();
        User user = userRepository.findByUserid(userid).orElseThrow(CUserNotFoundException::new);

        List<Student> students = studentService.addStudent(add_students.getStudents(), user);
        return responseService.getListResult(students);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PutMapping("/students")
    public CommonResult modifyStudent() {
//       학부모 밑으로 소속된 원생정보 수정
        return null;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @DeleteMapping("/students")
    public CommonResult deleteStudent() {
        //       학부모 밑으로 소속된 원생정보 삭제
        return null;
    }

}


