package com.kindergarten.api.student;

import com.kindergarten.api.common.exception.CKinderGartenNotFoundException;
import com.kindergarten.api.common.exception.CNotOwnerException;
import com.kindergarten.api.common.exception.CStudentNotFoundException;
import com.kindergarten.api.common.exception.CUserNotFoundException;
import com.kindergarten.api.kindergartens.KinderGarten;
import com.kindergarten.api.kindergartens.KinderGartenRepository;
import com.kindergarten.api.kindergartens.KinderGartenService;
import com.kindergarten.api.users.User;
import com.kindergarten.api.users.UserDTO;
import com.kindergarten.api.users.UserRepository;
import com.kindergarten.api.users.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {
    private final StudentRepository studentRepository;
    private final KinderGartenRepository kinderGartenRepository;
    private final KinderGartenService kinderGartenService;
    private final UserRepository userRepository;
    private final StudentLogRepository studentLogRepository;

    @Transactional// 회원가입시 유치원 회원검사
    public List<Student> addStudent(List<UserDTO.ADD_Student> student, User user) {

        List<Student> students = new ArrayList<>();
        for (UserDTO.ADD_Student create : student) {

            Student newstudent = new Student();
            KinderGarten kinderGarten = kinderGartenRepository.findById(create.getKindergarten_id()).orElseThrow(CKinderGartenNotFoundException::new);
            newstudent.setName(create.getName());
            newstudent.setBirthday(LocalDate.of(create.getYear(), create.getMonth(), create.getDay()));
            newstudent.setKinderGarten(kinderGarten);
            newstudent.setUser(user);

            students.add(newstudent);
        }
        studentRepository.saveAll(students);
        return students;
    }

    @Transactional
    public List<Student> findByKinderGartenStudnet(Long id) {
        KinderGarten kindergraten = kinderGartenService.findById(id);
        List<Student> byKinderGarten = studentRepository.findByKinderGarten(kindergraten);
        return byKinderGarten;
    }

    @Transactional
    public String modifyStudent(UserDTO.Modify_Student modify_student, String name) {
        User user = userRepository.findByUserid(name).orElseThrow(CUserNotFoundException::new);
        Student student = studentRepository.findById(modify_student.getStudentId()).orElseThrow(CStudentNotFoundException::new);
        if (user.getId().equals(student.getUser().getId())) {
            student.setBirthday(LocalDate.of(modify_student.getYear(), modify_student.getMonth(), modify_student.getDay()));
            KinderGarten kinderGarten = kinderGartenRepository.findById(modify_student.getKindergartenId()).orElseThrow(CKinderGartenNotFoundException::new);
//            유치원의 정보를 수정하려고 변경전 상황을 studentLog에 저장하고  인증권한을 false로 바꾼다.
            if (!kinderGarten.getId().equals(student.getKinderGarten().getId())) {

                StudentLog studentLog = new StudentLog();
                studentLog.setAccess(student.getAccess());
                studentLog.setStartDate(student.getCreatedDate());
                studentLog.setKinderGarten(student.getKinderGarten());
                studentLog.setStudents(student);
                studentLogRepository.save(studentLog);

                student.setAccess(false);
            }
            student.setKinderGarten(kinderGarten);
            student.setName(modify_student.getStudentName());
        } else {
            throw new CNotOwnerException();
        }
        Student save = studentRepository.save(student);
        return "수정완료";
    }

    @Transactional
    public String deleteStudent(String name, Long studentId) {
        User user = userRepository.findByUserid(name).orElseThrow(CUserNotFoundException::new);

        Student student = studentRepository.findById(studentId).orElseThrow(CStudentNotFoundException::new);
        String deleteStudentName = student.getName();

        if (student.getUser().getId().equals(user.getId())) {
//            포함된 로그 부터 삭제
            Optional<StudentLog> byStudents = studentLogRepository.findByStudents(student);
            byStudents.ifPresent(studentLogRepository::delete);
            studentRepository.delete(student);
        } else {
            throw new CNotOwnerException();
        }
        return deleteStudentName;
    }

    public List<UserDTO.Response_Student> findKinderStudents(String userid, long kindergartensID) {
        User user = userRepository.findByUserid(userid).orElseThrow(CUserNotFoundException::new);
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
        return students;

    }

}
