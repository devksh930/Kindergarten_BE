package com.kindergarten.api.management;

import com.kindergarten.api.common.exception.CAuthenticationEntryPointException;
import com.kindergarten.api.common.exception.CUserNotFoundException;
import com.kindergarten.api.kindergartens.KinderGartenRepository;
import com.kindergarten.api.student.Student;
import com.kindergarten.api.student.StudentRepository;
import com.kindergarten.api.users.User;
import com.kindergarten.api.users.UserDTO;
import com.kindergarten.api.users.UserRepository;
import com.kindergarten.api.users.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ManageMentService {
    private final UserRepository userRepository;
    private final KinderGartenRepository kinderGartenRepository;
    private final StudentRepository studentRepository;

    //원장과 admin이 설정할수 있는 원장 또는 선생님에 대한 인증권한
    public List<String> getRole(String userid) {
        User user = userRepository.findByUserid(userid).orElseThrow(CUserNotFoundException::new);
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
        return roles;
    }

    public List<UserDTO.Response_Student> findAccessStudent(Boolean access) {

        List<Student> byAccessTrue = studentRepository.findByAccess(access);
        List<UserDTO.Response_Student> accesslist = new ArrayList<>();

        for (Student student : byAccessTrue) {
            UserDTO.Response_Student response_student = new UserDTO.Response_Student();
            response_student.setUsername(student.getUser().getName());
            response_student.setUserphone(student.getUser().getPhone());
            response_student.setStudentId(student.getId());
            response_student.setName(student.getName());
            response_student.setBirthday(student.getBirthday());
            response_student.setCreated_date(student.getCreatedDate());
            response_student.setKindergarten_id(student.getKinderGarten().getId());
            response_student.setKindergarten_name(student.getKinderGarten().getName());
            response_student.setAccess(student.getAccess());
            accesslist.add(response_student);
        }
        return accesslist;
    }

    public List<UserDTO.UserRoleFind> findByRoleUser(UserRole role) {
        List<User> byRole = userRepository.findByRole(role);
        List<UserDTO.UserRoleFind> roleUserList = new ArrayList<>();
        for (User user : byRole) {
            UserDTO.UserRoleFind userRoleFind = new UserDTO.UserRoleFind();
            userRoleFind.setId(user.getId());
            userRoleFind.setUserid(user.getUserid());
            userRoleFind.setPhone(user.getPhone());
            userRoleFind.setName(user.getName());
            userRoleFind.setRole(user.getRole());
            userRoleFind.setKindergartenid(user.getKinderGarten().getId());
            userRoleFind.setKindergartenname(user.getKinderGarten().getName());
            roleUserList.add(userRoleFind);
        }
        return roleUserList;
    }
}
