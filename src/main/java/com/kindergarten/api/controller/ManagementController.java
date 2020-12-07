package com.kindergarten.api.controller;

import com.kindergarten.api.common.exception.CNotOwnerException;
import com.kindergarten.api.common.exception.CStudentNotFoundException;
import com.kindergarten.api.common.exception.CUserNotFoundException;
import com.kindergarten.api.common.result.CommonResult;
import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.management.ManageMentService;
import com.kindergarten.api.student.Student;
import com.kindergarten.api.student.StudentRepository;
import com.kindergarten.api.student.StudentService;
import com.kindergarten.api.users.*;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@RestController
@RequestMapping("/api/management")
@EnableSwagger2
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class ManagementController {
    private final ResponseService responseService;
    private final UserService userService;
    private final ManageMentService manageMentService;
    private final StudentService studentService;
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    //원장과 admin이 설정할수 있는 원장 또는 선생님에 대한 인증권한
    @GetMapping("/getrole")
    public CommonResult getRole() {
        //설정할수 있는 권한
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<String> role = manageMentService.getRole(authentication.getName());
        return responseService.getSingleResult(role);

    }

    //유치원 소속 선생님 가져오기
    //GET: /api/management/1?role=ROLE_NOT_PERMITTED_TEACHER || ROLE_TEACHER ||ROLE_NOT_PERMITTED_DIRECTOR ||ROLE_DIRECTOR
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/kindergartens/{kindergartensID}/users")
    public CommonResult getTeacher(@PathVariable Long kindergartensID, @RequestParam UserRole role) {
        List<UserDTO.Teacher_response> teacher = userService.getTeacher(kindergartensID, role);
        return responseService.getListResult(teacher);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    //유치원 소속 학생 가져오기
    @GetMapping("/kindergartens/{kindergartensID}/students")
    public CommonResult getStudent(@PathVariable Long kindergartensID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<UserDTO.findKinderStudents> students = studentService.findKinderStudents(authentication.getName(), kindergartensID);
        return responseService.getSingleResult(students);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    //선생 원장에 대한 권한변경

    @PutMapping("/users/{userid}/role")
    public CommonResult modifyRole(@PathVariable Long userid, @RequestParam UserRole role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userService.modifyRole(authentication.getName(), userid, role);
        return responseService.getSingleResult("권한이 " + role + "으로 변경되었습니다");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PutMapping("/students/{studentid}")
    public CommonResult modifyStudentAccess(@PathVariable Long studentid, @RequestParam Boolean access) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserid(authentication.getName()).orElseThrow(CUserNotFoundException::new);
        Student student = studentRepository.findById(studentid).orElseThrow(CStudentNotFoundException::new);

        if (student.getKinderGarten().getId().equals(user.getKinderGarten().getId())) {
            student.setAccess(access);
        } else {
            throw new CNotOwnerException();
        }
        Student save = studentRepository.save(student);

        return responseService.getSingleResult(student.getName() + "student" + save.getAccess());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/student/access/{access}")
    public CommonResult getStudentByAccess(@PathVariable Boolean access) {

        List<UserDTO.Response_Student> accessStudent = manageMentService.findAccessStudent(access);
        return responseService.getSingleResult(accessStudent);

    }

    @GetMapping("/users/role/{role}")
    public CommonResult getUserByRole(@PathVariable UserRole role) {
        List<UserDTO.UserRoleFind> byRoleUser = manageMentService.findByRoleUser(role);
        return responseService.getSingleResult(byRoleUser);
    }
}
