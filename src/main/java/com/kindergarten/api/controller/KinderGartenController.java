package com.kindergarten.api.controller;

import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.model.dto.KinderGartenDTO;
import com.kindergarten.api.model.entity.KinderGarten;
import com.kindergarten.api.model.entity.Student;
import com.kindergarten.api.service.KinderGartenService;
import com.kindergarten.api.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@RestController
@RequestMapping("/api/kindergartens")
@EnableSwagger2
@Slf4j
@CrossOrigin("*")

public class KinderGartenController {

    private final KinderGartenService kinderGartenService;
    private final ResponseService responseService;
    private final StudentService studentService;
    private final ModelMapper modelMapper;

    public KinderGartenController(KinderGartenService kinderGartenService, ResponseService responseService, StudentService studentService, ModelMapper modelMapper) {
        this.kinderGartenService = kinderGartenService;
        this.responseService = responseService;
        this.studentService = studentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/name") //GET:/api/kindergartens&name
    public SingleResult<KinderGartenDTO.UserCreate> signupfindbyName(@RequestParam(value = "name") String name, Pageable pageable) {
        KinderGartenDTO.UserCreate byAllByName = kinderGartenService.findByAllByName(name, pageable);
        return responseService.getSingleResult(byAllByName);
    }

    @GetMapping("/addr")//GET:/api/kindergartens/addr&addr
    public SingleResult<KinderGartenDTO.UserCreate> sginupfindbyAdress(@RequestParam(value = "addr") String addr, Pageable pageable) {
        KinderGartenDTO.UserCreate byAllByName = kinderGartenService.findByAddress(addr, pageable);
        return responseService.getSingleResult(byAllByName);
    }

    @GetMapping("/{id}")//GET:/api/kindergartens/{id}
    public SingleResult detailKindergarten(@PathVariable Long id) {
        KinderGarten byId = kinderGartenService.findById(id);
        KinderGartenDTO.KinderGartenDetail map = modelMapper.map(byId, KinderGartenDTO.KinderGartenDetail.class);
        return responseService.getSingleResult(map);
    }

    @GetMapping("{id}/student")//GET:/api/kindergartens/{id}/studnet
    public List<Student> findStudentKinder(@PathVariable Long id) {

        List<Student> byKinderGarten = studentService.findByKinderGartenStudnet(id);

        return byKinderGarten;
    }
}
