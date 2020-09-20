package com.kindergarten.api.controller;

import com.kindergarten.api.model.dto.KinderGartenDTO;
import com.kindergarten.api.model.entity.KinderGarten;
import com.kindergarten.api.service.KinderGartenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/kindergartens")
public class KinderGartenController {
}
