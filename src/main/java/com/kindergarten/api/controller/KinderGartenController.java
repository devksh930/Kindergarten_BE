package com.kindergarten.api.controller;

import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.model.dto.KinderGartenDTO;
import com.kindergarten.api.model.entity.KinderGarten;
import com.kindergarten.api.service.KinderGartenService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

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
    private ModelMapper modelMapper;

    @GetMapping //GET:/api/kindergartens{PathVariable}
    public SingleResult<KinderGartenDTO.UserCreate> finbydallByname(@RequestParam(value = "name") String name, Pageable pageable) {
        KinderGartenDTO.UserCreate byAllByName = kinderGartenService.findByAllByName(name, pageable);
        return responseService.getSingleResult(byAllByName);
    }

    @GetMapping("/addr")//GET:/api/kindergartens/addr{PathVariable}
    public SingleResult<KinderGartenDTO.UserCreate> findByAdress(@RequestParam(value = "addr") String addr, Pageable pageable) {
        KinderGartenDTO.UserCreate byAllByName = kinderGartenService.findByAddress(addr, pageable);
        return responseService.getSingleResult(byAllByName);
    }

    @GetMapping("/{id}")
    public SingleResult detailKinderGarten(@PathVariable Long id) {
        KinderGarten byId = kinderGartenService.findById(id);
        KinderGartenDTO.KinderGartenDetail kinderGartenDetail = modelMapper.map(byId, KinderGartenDTO.KinderGartenDetail.class);

        return responseService.getSingleResult(byId);
    }

}
