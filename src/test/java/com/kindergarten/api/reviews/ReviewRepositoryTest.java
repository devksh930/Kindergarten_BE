package com.kindergarten.api.reviews;

import com.kindergarten.api.common.exception.CResorceNotfoundException;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("dev")
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

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
}
