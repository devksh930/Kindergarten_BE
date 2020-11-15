package com.kindergarten.api.reviews.comment;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("dev")
public class ReviewCommentRepositoryTest {

    @Autowired
    ReviewCommentRepository reviewCommentRepository;

    @AfterEach
    public void cleanup() { // 데이터 섞임 방지
        reviewCommentRepository.deleteAll();
    }

    @Test
    public void createReviewComment() throws Exception {

    }
}