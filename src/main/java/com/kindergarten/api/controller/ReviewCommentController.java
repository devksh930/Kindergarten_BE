package com.kindergarten.api.controller;

import com.kindergarten.api.common.result.CommonResult;
import com.kindergarten.api.common.result.ResponseService;
import com.kindergarten.api.common.result.SingleResult;
import com.kindergarten.api.reviews.comment.CommentDTO;
import com.kindergarten.api.reviews.comment.ReviewCommentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    private final ResponseService responseService;
    private final ReviewCommentService reviewCommentService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/{reviewid}")
    @ResponseStatus(HttpStatus.CREATED)
    public SingleResult<String> createReviewComment(@PathVariable long reviewid, @RequestBody CommentDTO.CommentCreate response, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long reviewComment = reviewCommentService.createReviewComment(authentication.getName(), reviewid, response, pageable);
        return responseService.getSingleResult(reviewComment.toString());
    }

    @GetMapping("/{reviewid}")
    public SingleResult<CommentDTO.CommentPaging> getReviewComment(@PathVariable long reviewid, Pageable pageable) {
        CommentDTO.CommentPaging response = reviewCommentService.getReviewComments(reviewid, pageable);
        return responseService.getSingleResult(response);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @PutMapping("/{reviewid}")
    public CommonResult modifyReviewComment(@PathVariable long reviewid, @RequestBody CommentDTO.CommentModify commentModify) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        reviewCommentService.modifyComment(reviewid, authentication.getName(), commentModify);

        return responseService.getSuccessResult();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
    })
    @DeleteMapping("/{reviewid}")
    public CommonResult deleteRevieComment(@PathVariable long reviewid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        reviewCommentService.deleteComment(reviewid, authentication.getName());
        return responseService.getSuccessResult();
    }
}
