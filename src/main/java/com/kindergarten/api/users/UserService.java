package com.kindergarten.api.users;

import com.kindergarten.api.common.exception.*;
import com.kindergarten.api.kindergartens.KinderGartenRepository;
import com.kindergarten.api.security.util.JwtTokenProvider;
import com.kindergarten.api.student.Student;
import com.kindergarten.api.student.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    private final KinderGartenRepository kinderGartenRepository;
    private final StudentService studentService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


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
    public User modifyUser(String userid, UserDTO.UserModify userModify) {
        User updateUser = userRepository.findByUserid(userid).orElseThrow(CUserNotFoundException::new);
        String password = userModify.getPassword();
        String newPassword = userModify.getNewpassword();
        if (!passwordEncoder.matches(password, updateUser.getPassword())) {
            throw new CUserIncorrectPasswordException();
        }
        if (!userModify.getNewpassword().isBlank()) {
            updateUser.setPassword(passwordEncoder.encode(newPassword));
        }
        updateUser.setPhone(userModify.getPhone());
        updateUser.setEmail(userModify.getEmail());

        userRepository.save(updateUser);

        return updateUser;
    }

    @Transactional
    public User modifyTeacherKinder(String userid, UserDTO.TeacherModify teacherModify) {
        User teacher = userRepository.findByUserid(userid).orElseThrow(CUserNotFoundException::new);
        //        user의 권한이 user인경우 exception
        if (teacher.getRole().equals(UserRole.ROLE_USER)) {
            throw new CNotOwnerException();
        }
//        user의 유치원과 modify의 유치원 비교
        if (!teacher.getKinderGarten().getId().equals(teacherModify.getKindergraten_id())) {
            teacher.setKinderGarten(kinderGartenRepository.findById(teacherModify.getKindergraten_id()).orElseThrow(CKinderGartenNotFoundException::new));
            if (teacher.getRole().equals(UserRole.ROLE_TEACHER)) {
                teacher.setRole(UserRole.ROLE_NOT_PERMITTED_TEACHER);
            }
            if (teacher.getRole().equals(UserRole.ROLE_DIRECTOR)) {
                teacher.setRole(UserRole.ROLE_NOT_PERMITTED_DIRECTOR);
            }
            userRepository.save(teacher);
        } else {
            throw new CResorceNotfoundException();
        }
        return teacher;
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
    public UserDTO.Response_User_Student parentStudents(String userid) {
        User user = userRepository.findByUserid(userid).orElseThrow(CUserNotFoundException::new);
        UserDTO.Response_User_Student response_user_student = new UserDTO.Response_User_Student();
        response_user_student.setUserid(user.getUserid());

        List<UserDTO.Response_Student> students1 = response_user_student.getStudents();
        List<Student> students = user.getStudent();

        for (Student student : students) {
            UserDTO.Response_Student responseStudent = new UserDTO.Response_Student();
            responseStudent.setStudentId(student.getId());
            responseStudent.setName(student.getName());
            responseStudent.setAccess(student.getAccess());
            responseStudent.setBirthday(student.getBirthday());
            responseStudent.setKindergarten_id(student.getKinderGarten().getId());
            responseStudent.setKindergarten_name(student.getKinderGarten().getName());
            students1.add(responseStudent);
        }
        return response_user_student;
    }

    public UserDTO.currentUser currentUser(String userid) {
        UserDTO.currentUser currentUser = new UserDTO.currentUser();
        User user = userRepository.findByUserid(userid).orElseThrow(CUserNotFoundException::new);
        currentUser.setUserid(user.getUserid());
        currentUser.setName(user.getName());
        currentUser.setRole(user.getRole().name());

        return currentUser;
    }
}
