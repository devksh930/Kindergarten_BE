package com.kindergarten.api.controller;

import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.model.entity.Student;
import com.kindergarten.api.service.KinderGartenService;
import com.kindergarten.api.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public class KinderGartenController {
    private final KinderGartenService kinderGartenService;
    private final ResponseService responseService;
    private final StudentService studentService;

    public KinderGartenController(KinderGartenService kinderGartenService, ResponseService responseService, StudentService studentService) {
        this.kinderGartenService = kinderGartenService;
        this.responseService = responseService;
        this.studentService = studentService;
    }

    @GetMapping("{id}/student")//GET:/api/kindergartens/{id}/studnet
    public List<Student> findStudentKinder(@PathVariable Long id) {

        List<Student> byKinderGarten = studentService.findByKinderGartenStudnet(id);

        return byKinderGarten;
    }
}
