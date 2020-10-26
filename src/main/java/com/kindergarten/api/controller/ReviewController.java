package com.kindergarten.api.controller;

import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.model.entity.KinderGarten;
import com.kindergarten.api.model.entity.Student;
import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.repository.KinderGartenRepository;
import com.kindergarten.api.repository.StudentRepository;
import com.kindergarten.api.repository.UserRepository;
import com.kindergarten.api.security.util.CookieUtil;
import com.kindergarten.api.security.util.JwtUtil;
import com.kindergarten.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/kindergartens")
@EnableSwagger2
@Slf4j
//@CrossOrigin(origins = "https://mommyogi.com")
@CrossOrigin("*")

public class ReviewController {
    private final ModelMapper modelMapper;

    private final UserService userService;

    private final UserRepository userRepository;

    private final ResponseService responseService;
    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;
    private final StudentRepository studentRepository;
    private final KinderGartenRepository kinderGartenRepository;

    public ReviewController(ModelMapper modelMapper, UserService userService, UserRepository userRepository, ResponseService responseService, CookieUtil cookieUtil, JwtUtil jwtUtil, StudentRepository studentRepository, KinderGartenRepository kinderGartenRepository) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.userRepository = userRepository;
        this.responseService = responseService;
        this.cookieUtil = cookieUtil;
        this.jwtUtil = jwtUtil;
        this.studentRepository = studentRepository;

        this.kinderGartenRepository = kinderGartenRepository;
    }

    @PostMapping("/{kindergartenid}/reviews")
    public List<Student> createReview(HttpServletRequest request, /*@RequestBody UserDTO.Create createreview, */@PathVariable Long kindergartenid) {
        String refreshToken = cookieUtil.getCookie(request, "refreshToken").getValue();
        String tokenUserid = jwtUtil.getUserid(refreshToken);
        User userid = userService.findUserid(tokenUserid);

        Optional<KinderGarten> byId1 = kinderGartenRepository.findById(kindergartenid);

        List<Student> byUser = studentRepository.findByUserAndKinderGarten(userid, byId1.get());
        List<Student> byUserAndKinderGartenAndAccessTrue = studentRepository.findByUserAndKinderGartenAndAccessTrue(userid, byId1.get());
        boolean user1 = studentRepository.existsByUser(userid);
        boolean user2 = studentRepository.existsByUserAndAccessTrue(userid);
        if (byUser.isEmpty()) {
            log.error("========================================================================");
            log.error("리뷰작성불가" + user1);
            log.error("리뷰작성불가" + user2);
            log.error("========================================================================");
        } else if (!byUserAndKinderGartenAndAccessTrue.isEmpty()) {
            log.error("========================================================================");
            log.error("실 이용자 리뷰" + user1);
            log.error("리뷰작성불가" + user2);
            log.error("========================================================================");
        } else {
            log.error("========================================================================");
            log.error("확인되지 않은 이용자 리뷰" + user1);
            log.error("리뷰작성불가" + user2);
            log.error("========================================================================");
        }

        return byUserAndKinderGartenAndAccessTrue;
    }
}
