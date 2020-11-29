package com.kindergarten.api.reviews;

import com.kindergarten.api.common.exception.CKinderGartenNotFoundException;
import com.kindergarten.api.common.exception.CResorceNotfoundException;
import com.kindergarten.api.common.exception.CUserNotFoundException;
import com.kindergarten.api.kindergartens.KinderGarten;
import com.kindergarten.api.kindergartens.KinderGartenRepository;
import com.kindergarten.api.student.Student;
import com.kindergarten.api.users.User;
import com.kindergarten.api.users.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("dev")
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private KinderGartenRepository kinderGartenRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    @AfterEach
    public void cleanup() { // 데이터 섞임 방지
        reviewRepository.deleteAll();
    }


    @Test
    public void createReview() {
        Review review = new Review();

        review.setId(1L);
        review.setBadThing("나쁜점");
        review.setGoodThing("좋은점");
        review.setEduScore(5);
        review.setTeacherScore(5);
        review.setDescScore(5);
        review.setDescription("총평");
        review.setFacilityScore(5);
        review.setAccessInfo(AccessInfo.ACCESS);
        review.setDescScore(5);
        reviewRepository.save(review);

        Review findReview = reviewRepository.findById(1L).orElseThrow(CResorceNotfoundException::new);

        Assertions.assertThat(review.getId()).isEqualTo(findReview.getId());
    }


    @Test
    public void updateReview() {
        Review review = new Review();

        review.setBadThing("나쁜점");
        review.setGoodThing("좋은점");
        review.setEduScore(5);
        review.setTeacherScore(5);
        review.setDescScore(5);
        review.setDescription("총평");
        review.setFacilityScore(5);
        review.setAccessInfo(AccessInfo.ACCESS);
        review.setDescScore(5);
        reviewRepository.save(review);

        Review updateReview = reviewRepository.findById(1L).orElseThrow(CResorceNotfoundException::new);
        updateReview.setBadThing("나쁜점입니다.");
        Review update = reviewRepository.save(updateReview);
        Assertions.assertThat(updateReview.getBadThing()).isEqualTo(update.getBadThing());
    }

    @Test
    public void deleteReview() {
        Review review = new Review();

        review.setId(9999999999L);
        review.setBadThing("나쁜점");
        review.setGoodThing("좋은점");
        review.setEduScore(5);
        review.setTeacherScore(5);
        review.setDescScore(5);
        review.setDescription("총평");
        review.setFacilityScore(5);
        review.setAccessInfo(AccessInfo.ACCESS);
        review.setDescScore(5);
        reviewRepository.save(review);


        reviewRepository.delete(review);

        Optional<Review> deleteReview = reviewRepository.findById(9999999999L);

        Assert.assertFalse(deleteReview.isPresent());
    }

    @Test
    public void existsByUserAndKinderGarten() {
        User user = userRepository.findById(1L).orElseThrow(CUserNotFoundException::new);
        List<Student> student = user.getStudent();
        KinderGarten kinderGarten = student.get(1).getKinderGarten();
        boolean b = reviewRepository.existsByUserAndKinderGarten(user, kinderGarten);
        Assertions.assertThat(b).isEqualTo(b);
    }

    @Test
    public void findByKinderGarten() {
        KinderGarten kinderGarten = kinderGartenRepository.findById(1L).orElseThrow(CKinderGartenNotFoundException::new);
        Pageable page = PageRequest.of(1, 10);
        Page<Review> byKinderGarten = reviewRepository.findByKinderGarten(kinderGarten, page);
        byKinderGarten.getSize();
        byKinderGarten.getContent().isEmpty();
        Assertions.assertThat(byKinderGarten.getSize()).isEqualTo(10);
        Assertions.assertThat(byKinderGarten.getContent().isEmpty()).isEqualTo(true);

    }
}
