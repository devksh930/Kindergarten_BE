package com.kindergarten.api.controller;

import com.kindergarten.api.common.result.CommonResult;
import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.model.dto.ReviewDTO;
import com.kindergarten.api.repository.KinderGartenRepository;
import com.kindergarten.api.repository.ReviewRepository;
import com.kindergarten.api.repository.StudentRepository;
import com.kindergarten.api.repository.UserRepository;
import com.kindergarten.api.security.util.JwtTokenProvider;
import com.kindergarten.api.service.ReviewService;
import com.kindergarten.api.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@RequestMapping("/api/reviews")
@EnableSwagger2
@Slf4j
//@CrossOrigin(origins = "https://mommyogi.com")
@CrossOrigin("*")

public class ReviewController {
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final UserRepository userRepository;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtUtil;
    private final StudentRepository studentRepository;
    private final KinderGartenRepository kinderGartenRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    public ReviewController(ModelMapper modelMapper, UserService userService, UserRepository userRepository, ResponseService responseService, JwtTokenProvider jwtUtil, StudentRepository studentRepository, KinderGartenRepository kinderGartenRepository, ReviewRepository reviewRepository, ReviewService reviewService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.userRepository = userRepository;
        this.responseService = responseService;
        this.jwtUtil = jwtUtil;
        this.studentRepository = studentRepository;
        this.kinderGartenRepository = kinderGartenRepository;
        this.reviewRepository = reviewRepository;
        this.reviewService = reviewService;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/check/{kindergarten_id}/{student_id}")//리뷰 작성 가능여부
    public CommonResult reviewPossibleStatus(@PathVariable String kindergarten_id, @PathVariable String student_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        ReviewDTO.CheckResponse checkResponse = reviewService.reviewstatusCheck(authentication, kindergarten_id, student_id);

        return responseService.getSingleResult(checkResponse);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping //리뷰 작성
    public CommonResult createReview(@RequestBody ReviewDTO.CreateReview createReview) {
        ReviewDTO.CreateResponse createResponse = reviewService.reviewCreate(createReview);

        return responseService.getSingleResult(createResponse);
    }
}
