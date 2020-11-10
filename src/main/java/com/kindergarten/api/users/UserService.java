package com.kindergarten.api.users;

import com.kindergarten.api.common.exception.CUserExistException;
import com.kindergarten.api.common.exception.CUserIncorrectPasswordException;
import com.kindergarten.api.common.exception.CUserNotFoundException;
import com.kindergarten.api.student.Student;
import com.kindergarten.api.kindergartens.KinderGartenRepository;
import com.kindergarten.api.security.util.JwtTokenProvider;
import com.kindergarten.api.student.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;


    private final KinderGartenRepository kinderGartenRepository;
    private final StudentService studentService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, KinderGartenRepository kinderGartenRepository, StudentService studentService, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.kinderGartenRepository = kinderGartenRepository;
        this.studentService = studentService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //id가 중복된 회원이 있는지 검사
    public boolean isexistsByUserid(String userid) {
        Boolean byUserid = userRepository.existsByUserid(userid);
        if (byUserid) {
            throw new CUserExistException();
        }
        return false;
    }

    @Transactional //회원가입전 가입하려고 하는 회원의 Role, 중복 회원검사
    public User registerAccount(UserDTO.Create userdto) {
        userRepository.findByUserid(userdto.getUserid()).ifPresent(user -> {
            throw new CUserExistException();
        });
        User user = new User();
        if (userdto.getRole().equals("USER")) {
            user = this.signUpParent(userdto);

        }
        if (userdto.getRole().equals("TEACHER") || userdto.getRole().equals("DIRECTOR")) {
            user = this.signUpTeacher(userdto);
        }
        return user;
    }

    @Transactional // 선생님 회원가입
    public User signUpTeacher(UserDTO.Create userdto) {
        User newuser = new User();
        newuser.setUserid(userdto.getUserid());
        newuser.setRole(UserRole.ROLE_NOT_PERMITTED_TEACHER);

        if (userdto.getRole().equals("DIRECTOR")) {
            newuser.setRole(UserRole.ROLE_NOT_PERMITTED_DIRECTOR);
        }
        newuser.setPhone(userdto.getPhone());
        newuser.setKinderGarten(kinderGartenRepository.findById(Long.valueOf(userdto.getKindergarten_id())).get());
        newuser.setName(userdto.getName());
        newuser.setEmail(userdto.getEmail());
        newuser.setPassword(passwordEncoder.encode(userdto.getPassword()));
        userRepository.save(newuser);

        return newuser;
    }

    @Transactional// 학부모 회원가입
    public User signUpParent(UserDTO.Create userdto) {

        User newuser = new User();
        newuser.setUserid(userdto.getUserid());
        newuser.setRole(UserRole.ROLE_USER);
        newuser.setPhone(userdto.getPhone());
        newuser.setName(userdto.getName());
        newuser.setEmail(userdto.getEmail());
        newuser.setPassword(passwordEncoder.encode(userdto.getPassword()));


        userRepository.save(newuser);

        if (!userdto.getStudent().isEmpty()) {
            List<UserDTO.ADD_Student> students = userdto.getStudent();

            List<Student> newstudent = studentService.addStudent(students, newuser);
            newuser.setStudent(newstudent);
        }

        return newuser;
    }

    @Transactional // 회원정보 수정
    public User modifyUser(Authentication authentication, UserDTO.UserModify userModify) {
        String userId = authentication.getName();
        User updateUser = userRepository.findByUserid(userId).orElseThrow(CUserNotFoundException::new);
        String phone = userModify.getPhone();
        String email = userModify.getEmail();
        String kindergarten_id = userModify.getKindergraten_id();

        if (!userModify.getPhone().isBlank()) {
            updateUser.setPhone(phone);
        }
        if (!userModify.getEmail().isBlank()) {
            updateUser.setEmail(email);
        }
        if (!userModify.getKindergraten_id().isBlank()) {
            updateUser.setKinderGarten(kinderGartenRepository.findById(Long.valueOf((kindergarten_id))).get());
        }

        userRepository.save(updateUser);

        return updateUser;
    }

    @Transactional //로그인
    public UserDTO.Login_response loginUser(String userid, String password) {

        User user = userRepository.findByUserid(userid).orElseThrow(CUserNotFoundException::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CUserIncorrectPasswordException();
        }
        UserDTO.Login_response response = new UserDTO.Login_response();
        response.setName(user.getName());
        response.setToken(jwtTokenProvider.createToken(String.valueOf(user.getUserid()), user.getRole().name()));
        response.setUserid(user.getUserid());


        return response;
    }

    // 유저의 학생 불러오기
    public UserDTO.Response_User_Student parentStudents(Authentication authentication) {
        String name = authentication.getName();
        User user = userRepository.findByUserid(name).orElseThrow(CUserNotFoundException::new);
        UserDTO.Response_User_Student response_user_student = new UserDTO.Response_User_Student();
        response_user_student.setUserid(user.getUserid());

        List<UserDTO.Response_Student> students1 = response_user_student.getStudents();
        List<Student> students = user.getStudent();

        for (Student student : students) {
            UserDTO.Response_Student responseStudent = new UserDTO.Response_Student();
            responseStudent.setStudent_id(student.getId());
            responseStudent.setName(student.getName());
            responseStudent.setAccess(student.getAccess());
            responseStudent.setBirthday(student.getBirthday());
            responseStudent.setKindergarten_id(student.getKinderGarten().getId());
            responseStudent.setKindergarten_name(student.getKinderGarten().getName());
            students1.add(responseStudent);
        }
        return response_user_student;
    }
}
