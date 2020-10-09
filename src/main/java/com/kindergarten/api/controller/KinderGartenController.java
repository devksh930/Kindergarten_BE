package com.kindergarten.api.controller;

import com.kindergarten.api.common.result.PageResult;
import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.model.entity.KinderGarten;
import com.kindergarten.api.service.KinderGartenService;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

@RestController
@RequestMapping("/api/kindergartens")
@EnableSwagger2
@Slf4j
public class KinderGartenController {
    @Autowired
    private KinderGartenService kinderGartenService;

    @Autowired
    private ResponseService responseService;

    @GetMapping //GET:/api/kindergartens{PathVariable}
    public PageResult<KinderGarten> finbydallByname(@RequestParam(value = "name") String name, Pageable pageable) {
//        name = "부산";
        Page<KinderGarten> byAllByName = kinderGartenService.findByAllByName(name, pageable);
        return responseService.getPageResult(byAllByName);
    }
}
