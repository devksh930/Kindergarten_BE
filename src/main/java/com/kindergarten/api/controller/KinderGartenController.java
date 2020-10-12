package com.kindergarten.api.controller;

import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.model.dto.KinderGartenDTO;
import com.kindergarten.api.model.entity.KinderGarten;
import com.kindergarten.api.model.entity.Student;
import com.kindergarten.api.repository.KinderGartenRepository;
import com.kindergarten.api.repository.StudentRepository;
import com.kindergarten.api.service.KinderGartenService;
import com.kindergarten.api.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api/kindergartens")
@EnableSwagger2
@Slf4j
public class KinderGartenController {
    @Autowired
    private KinderGartenService kinderGartenService;

    @Autowired
    private ResponseService responseService;

    @Autowired
    private StudentService studentService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/name") //GET:/api/kindergartens&name
    public SingleResult<KinderGartenDTO.UserCreate> finbydallByname(@RequestParam(value = "name") String name, Pageable pageable) {
        KinderGartenDTO.UserCreate byAllByName = kinderGartenService.findByAllByName(name, pageable);
        return responseService.getSingleResult(byAllByName);
    }

    @GetMapping("/addr")//GET:/api/kindergartens/addr&addr
    public SingleResult<KinderGartenDTO.UserCreate> findByAdress(@RequestParam(value = "addr") String addr, Pageable pageable) {
        KinderGartenDTO.UserCreate byAllByName = kinderGartenService.findByAddress(addr, pageable);
        return responseService.getSingleResult(byAllByName);
    }

    @GetMapping("/{id}")//GET:/api/kindergartens/{id}
    public SingleResult detailKinderGarten(@PathVariable Long id) {
        KinderGarten byId = kinderGartenService.findById(id);
        KinderGartenDTO.KinderGartenDetail map = modelMapper.map(byId, KinderGartenDTO.KinderGartenDetail.class);
        return responseService.getSingleResult(map);
    }

    @GetMapping("{id}/student")//GET:/api/kindergartens/{id}/studnet
    public List<Student> findStudnetKinder(@PathVariable Long id) {

        List<Student> byKinderGarten = studentService.findByKinderGartenStudnet(id);

        return byKinderGarten;
    }
}
