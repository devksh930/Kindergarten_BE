package com.kindergarten.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@RequestMapping("/api/kindergartens")
@EnableSwagger2
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
public class ManagementController {

}
