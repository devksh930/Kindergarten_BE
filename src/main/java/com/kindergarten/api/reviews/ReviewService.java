package com.kindergarten.api.reviews;

import com.kindergarten.api.common.exception.*;
import com.kindergarten.api.kindergartens.KinderGarten;
import com.kindergarten.api.kindergartens.KinderGartenRepository;
import com.kindergarten.api.student.Student;
import com.kindergarten.api.student.StudentRepository;
import com.kindergarten.api.users.User;
import com.kindergarten.api.users.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ReviewRepository reviewRepository;
    private final KinderGartenRepository kinderGartenRepository;

    @Transactional
    public ReviewDTO.CheckResponse reviewstatusCheck(String userid, String role, Long kindergarten_id, Long student_id) {
        String ROLE = role.replace("[", "").replace("]", "");
        String name = userid;
        ReviewDTO.CheckResponse checkResponse = new ReviewDTO.CheckResponse();

        if (ROLE.equals("ROLE_USER") && !name.isBlank()) { //권한 검사
            User user = userRepository.findByUserid(name).orElseThrow(CUserNotFoundException::new);
            KinderGarten kinderGarten = kinderGartenRepository.findById(kindergarten_id).orElseThrow(CKinderGartenNotFoundException::new);
            Student student = studentRepository.findById(student_id).orElseThrow(CUserNotFoundException::new);

            if (user != student.getUser()) {
                throw new CNotOwnerException();
            }
            boolean existReview = reviewRepository.existsByUserAndKinderGarten(user, kinderGarten);


            if (existReview) { //해당유치원에 작성한 리뷰가 있으면
                throw new CReviewExistException();

            } else {// 해당유치원에 작성한 리뷰가 없을경우
                if (student.getKinderGarten().equals(kinderGarten)) {
                    checkResponse.setKindergarten_id(kinderGarten.getId());
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

    @Transactional
    public ReviewDTO.CreateResponse reviewCreate(ReviewDTO.CreateReview createReview) {

        User user = userRepository.findByUserid(createReview.getUser_id()).orElseThrow(CUserNotFoundException::new);
        KinderGarten kinderGarten = kinderGartenRepository.findById(Long.valueOf(createReview.getKinderGarten_id())).orElseThrow(CKinderGartenNotFoundException::new);
        boolean existsReview = reviewRepository.existsByUserAndKinderGarten(user, kinderGarten);

        if (existsReview) {
            throw new CReviewExistException();
        }

        ReviewDTO.CreateResponse reviewResponse = new ReviewDTO.CreateResponse();

        boolean userownstudent = studentRepository.existsByUserAndKinderGarten(user, kinderGarten);
        boolean studentAccess = studentRepository.existsByUserAndKinderGartenAndAccessTrue(user, kinderGarten);
        Review review = new Review();

        if (userownstudent) {
            review.setAccessInfo(AccessInfo.NOT_ACCESS);
            if (studentAccess) {
                review.setAccessInfo(AccessInfo.ACCESS);
            }
        } else {
            throw new CNotOwnerException();
        }
//        항목별 점수
        review.setDescScore(createReview.getDescScore());
        review.setEduScore(createReview.getEduScore());
        review.setFacilityScore(createReview.getFacilityScore());
        review.setTeacherScore(createReview.getTeacherScore());
//      장단점 서술
        review.setBadThing(createReview.getBadThing());
        review.setGoodThing(createReview.getGoodThing());
//      유치원 총평
        review.setKinderGarten(kinderGarten);
        review.setDescription(createReview.getDescription());
//        리뷰작성자
        review.setAnonymous(createReview.getAnonymous());
        review.setUser(user);

        Review save = reviewRepository.save(review);
        reviewResponse.setReviewindex(save.getId());


        return reviewResponse;
    }

    @Transactional
    public ReviewDTO.KindergartenReview kindergartenrReview(Long kindergarten_id, Pageable pageable) {
        KinderGarten kinderGarten = kinderGartenRepository.findById(kindergarten_id).orElseThrow(CKinderGartenNotFoundException::new);
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdDate"));

        Page<Review> byKinderGarten = reviewRepository.findByKinderGarten(kinderGarten, pageable);
        ReviewDTO.KindergartenReview kindergartenReview = new ReviewDTO.KindergartenReview();
        kindergartenReview.setTotalElements(byKinderGarten.getTotalElements());
        kindergartenReview.setFindReviews(byKinderGarten.getContent());
        kindergartenReview.setCurrentpage(byKinderGarten.getPageable().getPageNumber());
        kindergartenReview.setTotalPage(byKinderGarten.getTotalPages());

        return kindergartenReview;
    }

}
