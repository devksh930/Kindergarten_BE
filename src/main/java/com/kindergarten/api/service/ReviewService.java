package com.kindergarten.api.service;

import com.kindergarten.api.common.exception.*;
import com.kindergarten.api.model.dto.ReviewDTO;
import com.kindergarten.api.model.entity.KinderGarten;
import com.kindergarten.api.model.entity.Student;
import com.kindergarten.api.model.entity.User;
import com.kindergarten.api.repository.KinderGartenRepository;
import com.kindergarten.api.repository.ReviewRepository;
import com.kindergarten.api.repository.StudentRepository;
import com.kindergarten.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class ReviewService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ReviewRepository reviewRepository;
    private final KinderGartenRepository kinderGartenRepository;

    public ReviewService(UserRepository userRepository, StudentRepository studentRepository, ReviewRepository reviewRepository, KinderGartenRepository kinderGartenRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.reviewRepository = reviewRepository;
        this.kinderGartenRepository = kinderGartenRepository;
    }

    public ReviewDTO.CheckResponse reviewstatusCheck(Authentication authentication, String kindergarten_id, String student_id) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String ROLE = authorities.toString().replace("[", "").replace("]", "");
        String name = authentication.getName();
        ReviewDTO.CheckResponse checkResponse = new ReviewDTO.CheckResponse();

        if (ROLE.equals("ROLE_USER") && !name.isBlank()) {
            User user = userRepository.findByUserid(name).orElseThrow(CUserNotFoundException::new);
            KinderGarten kinderGarten = kinderGartenRepository.findById(Long.valueOf(kindergarten_id)).orElseThrow(CKinderGartenNotFoundException::new);
            Student student = studentRepository.findById(Long.valueOf(student_id)).orElseThrow(CUserNotFoundException::new);
            if (user == student.getUser()) {
                throw new CNotOwnerException();
            }
            boolean existReview = reviewRepository.existsByUserAndKinderGarten(user, kinderGarten);


            if (existReview) { //해당유치원에 작성한 리뷰가 없으면
                throw new CReviewExistException();

            } else if (!existReview) {
                if (student.getKinderGarten() == kinderGarten) {
                    checkResponse.setKindergarten_id(String.valueOf(kinderGarten.getId()));
                    checkResponse.setUser_id(String.valueOf(user.getId()));
                    checkResponse.setStatus("작성가능");
                } else
                    throw new CNotOwnerException();
            }
        } else { // ROLE이 유저가 아닐경우
            throw new CAuthenticationEntryPointException();
        }
        return checkResponse;
    }

}
