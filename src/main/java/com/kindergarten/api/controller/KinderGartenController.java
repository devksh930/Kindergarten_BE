package com.kindergarten.api.controller;

import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.model.dto.KinderGartenDTO;
import com.kindergarten.api.model.entity.KinderGarten;
import com.kindergarten.api.service.KinderGartenService;
import com.kindergarten.api.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
//유치원 상세조회
    @GetMapping("/{id}")//GET:/api/kindergartens/{id}
    public SingleResult detailKindergarten(@PathVariable Long id) {
        KinderGarten byId = kinderGartenService.findById(id);
        KinderGartenDTO.KinderGartenDetail map = modelMapper.map(byId, KinderGartenDTO.KinderGartenDetail.class);
        return responseService.getSingleResult(map);
    }

}
