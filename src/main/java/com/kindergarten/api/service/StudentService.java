package com.kindergarten.api.service;

import com.kindergarten.api.model.dto.UserDTO;
import com.kindergarten.api.model.entity.KinderGarten;
import com.kindergarten.api.model.entity.Student;
import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.repository.KinderGartenRepository;
import com.kindergarten.api.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StudentService {
    private final StudentRepository studentRepository;
    private final KinderGartenRepository kinderGartenRepository;
    private final KinderGartenService kinderGartenService;

    public StudentService(StudentRepository studentRepository, KinderGartenRepository kinderGartenRepository, KinderGartenService kinderGartenService) {
        this.studentRepository = studentRepository;
        this.kinderGartenRepository = kinderGartenRepository;
        this.kinderGartenService = kinderGartenService;
    }

    @Transactional// 회원가입시 유치원 회원검사
    public List<Student> addStudent(List<UserDTO.ADD_Student> student, User user) {

        List<Student> students = new ArrayList<>();
        for (UserDTO.ADD_Student create : student) {

            Student newstudent = new Student();
            KinderGarten kinderGarten = kinderGartenRepository.findById(Long.valueOf(create.getKindergarten_id())).get();
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

}
