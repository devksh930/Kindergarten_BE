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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@RequestMapping("/api/review")
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

//    @PostMapping("/{kindergartenid}/reviews")
//    public List<Student> createReview(@PathVariable Long kindergartenid) {
//
//
//        Optional<KinderGarten> byId1 = kinderGartenRepository.findById(kindergartenid);
//        KinderGarten kinderGarten = byId1.orElseThrow(CKinderGartenNotFoundException::new);
//
//        if (byUser.isEmpty()) {
//            log.error("========================================================================");
//            log.error("리뷰작성불가" + user1);
//            log.error("리뷰작성불가" + user2);
//            log.error("========================================================================");
//        } else if (!byUserAndKinderGartenAndAccessTrue.isEmpty()) {
//            log.error("========================================================================");
//            log.error("실 이용자 리뷰" + user1);
//            log.error("리뷰작성불가" + user2);
//            log.error("========================================================================");
//        } else {
//            log.error("========================================================================");
//            log.error("확인되지 않은 이용자 리뷰" + user1);
//            log.error("리뷰작성불가" + user2);
//            log.error("========================================================================");
//        }
//
//        return byUserAndKinderGartenAndAccessTrue;
//    }
}
