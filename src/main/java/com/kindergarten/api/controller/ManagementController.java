package com.kindergarten.api.controller;

import com.kindergarten.api.common.exception.CAuthenticationEntryPointException;
import com.kindergarten.api.common.exception.CKinderGartenNotFoundException;
import com.kindergarten.api.common.exception.CNotOwnerException;
import com.kindergarten.api.common.exception.CUserNotFoundException;
import com.kindergarten.api.common.result.CommonResult;
import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.kindergartens.KinderGarten;
import com.kindergarten.api.kindergartens.KinderGartenRepository;
import com.kindergarten.api.student.Student;
import com.kindergarten.api.student.StudentRepository;
import com.kindergarten.api.users.User;
import com.kindergarten.api.users.UserDTO;
import com.kindergarten.api.users.UserRepository;
import com.kindergarten.api.users.UserRole;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/management")
@EnableSwagger2
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class ManagementController {
    private final ResponseService responseService;
    private final UserRepository userRepository;
    private final KinderGartenRepository kinderGartenRepository;
    private final StudentRepository studentRepository;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    //원장과 admin이 설정할수 있는 원장 또는 선생님에 대한 인증권한
    @GetMapping("/getrole")
    public CommonResult getRole() {
        //설정할수 있는 권한
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserid(authentication.getName()).orElseThrow(CUserNotFoundException::new);
        List<String> roles = Stream.of(UserRole.values()).map(UserRole::name).collect(Collectors.toList());
        roles.remove(UserRole.ROLE_USER.name());
        if (user.getRole().equals(UserRole.ROLE_DIRECTOR) || user.getRole().equals(UserRole.ROLE_ADMIN)) {
            if (user.getRole().equals(UserRole.ROLE_DIRECTOR)) {
                roles.remove(UserRole.ROLE_ADMIN.name());
                roles.remove(UserRole.ROLE_DIRECTOR.name());
                roles.remove(UserRole.ROLE_NOT_PERMITTED_DIRECTOR.name());
            }
        } else {
            throw new CAuthenticationEntryPointException();
        }
        return responseService.getSingleResult(roles);

    }

    //유치원 소속 선생님 가져오기
    //GET: /api/management/1?role=ROLE_NOT_PERMITTED_TEACHER || ROLE_TEACHER ||ROLE_NOT_PERMITTED_DIRECTOR ||ROLE_DIRECTOR
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/{kindergartensID}")
    public CommonResult getTeacher(@PathVariable Long kindergartensID, @RequestParam UserRole role) {
        KinderGarten kinderGarten = kinderGartenRepository.findById(kindergartensID).orElseThrow(CKinderGartenNotFoundException::new);
        List<User> byKinderGartenTecaher = userRepository.findByKinderGartenAndRole(kinderGarten, role);
        List<UserDTO.Teacher_response> teacher_responses = new ArrayList<>();
        for (User user : byKinderGartenTecaher) {
            UserDTO.Teacher_response teacher_response = new UserDTO.Teacher_response();
            teacher_response.setName(user.getName());
            teacher_response.setROLE(user.getRole().name());
            teacher_response.setUserid(user.getUserid());
            teacher_responses.add(teacher_response);
        }
        return responseService.getListResult(teacher_responses);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    //유치원 소속 학생 가져오기
    @GetMapping("/{kindergartensID}/students")
    public CommonResult getStudent(@PathVariable Long kindergartensID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserid(authentication.getName()).orElseThrow(CUserNotFoundException::new);
        List<UserDTO.Response_Student> students = new ArrayList<>();
        if (!user.getKinderGarten().getId().equals(kindergartensID)) {
            throw new CNotOwnerException();
        }
        if (user.getRole().equals(UserRole.ROLE_TEACHER) || user.getRole().equals(UserRole.ROLE_DIRECTOR) || user.getRole().equals(UserRole.ROLE_ADMIN)) {

            KinderGarten kinderGarten = kinderGartenRepository.findById(kindergartensID).orElseThrow(CKinderGartenNotFoundException::new);
            List<Student> byKinderGarten = studentRepository.findByKinderGarten(kinderGarten);
            for (Student student : byKinderGarten) {
                UserDTO.Response_Student response_student = new UserDTO.Response_Student();
                response_student.setStudentId(student.getId());
                response_student.setAccess(student.getAccess());
                response_student.setStudentId(student.getId());
                response_student.setBirthday(student.getBirthday());
                response_student.setKindergarten_id(student.getKinderGarten().getId());
                response_student.setKindergarten_name(student.getKinderGarten().getName());
                response_student.setCreated_date(student.getCreatedDate());
                students.add(response_student);
            }

        } else {
            throw new CNotOwnerException();
        }
        return responseService.getSingleResult(students);
    }
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    //선생 원장에 대한 권한변경
    @PutMapping("/{userid}/role")
    public CommonResult modifyRole(@PathVariable Long userid, @RequestParam UserRole role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUserid(authentication.getName()).orElseThrow(CUserNotFoundException::new);
        User modifyUserRole = userRepository.findById(userid).orElseThrow(CUserNotFoundException::new);
        //권한이 ADMIN, DIRECTOR일것
        if (user.getRole().equals(UserRole.ROLE_DIRECTOR) && modifyUserRole.getKinderGarten().getId().equals(user.getKinderGarten().getId()) || user.getRole().equals(UserRole.ROLE_ADMIN)) {
            //DIRECTOR일경우 유치원이 같을것
            modifyUserRole.setRole(role);
        } else {
            throw new CNotOwnerException();
        }
        userRepository.save(modifyUserRole);
        return responseService.getSingleResult("권한이 " + role + "으로 변경되었습니다");
    }
}
