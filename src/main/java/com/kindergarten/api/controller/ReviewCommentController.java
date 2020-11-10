package com.kindergarten.api.controller;

import com.kindergarten.api.common.exception.CResorceNotfoundException;
import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.reviews.Review;
import com.kindergarten.api.reviews.ReviewRepository;
import com.kindergarten.api.reviews.comment.CommentDTO;
import com.kindergarten.api.reviews.comment.ReviewCommentRepository;
import com.kindergarten.api.reviews.comment.ReviewCommentService;
import com.kindergarten.api.users.UserRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@RestController
@RequestMapping("/api/comment")
@EnableSwagger2
@Slf4j
@RequiredArgsConstructor
public class ReviewCommentController {

    private final ReviewCommentRepository reviewCommentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ResponseService responseService;
    private final ReviewCommentService reviewCommentService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/{reviewid}")
    public SingleResult createReviewComment(@PathVariable long reviewid, @RequestBody CommentDTO.CommentCreate response, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long reviewComment = reviewCommentService.createReviewComment(authentication, reviewid, response, pageable);

        return responseService.getSingleResult(reviewComment.toString());
    }

    @GetMapping("/{reviewid}")
    public Page<CommentDTO.CommentRequest> getReviewComment(@PathVariable long reviewid, Pageable pageable) {
        Review review = reviewRepository.findById(reviewid).orElseThrow(CResorceNotfoundException::new);
        return reviewCommentRepository.findAllByReview(review, pageable).map(CommentDTO.CommentRequest::new);
    }
}
