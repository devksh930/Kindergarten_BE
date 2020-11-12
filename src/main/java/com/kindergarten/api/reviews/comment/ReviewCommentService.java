package com.kindergarten.api.reviews.comment;

import com.kindergarten.api.common.exception.CNotOwnerException;
import com.kindergarten.api.common.exception.CResorceNotfoundException;
import com.kindergarten.api.common.exception.CUserNotFoundException;
import com.kindergarten.api.reviews.Review;
import com.kindergarten.api.reviews.ReviewRepository;
import com.kindergarten.api.users.User;
import com.kindergarten.api.users.UserRepository;
import com.kindergarten.api.users.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewCommentService {
    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final UserRepository userRepository;

    public long createReviewComment(Authentication authentication, long reviewid, CommentDTO.CommentCreate response, Pageable pageable) {
        String name = authentication.getName();
        User user = userRepository.findByUserid(name).orElseThrow(CUserNotFoundException::new);
        Review review = reviewRepository.findById(reviewid).orElseThrow(CResorceNotfoundException::new);
//        권한이 유저일경
        if (user.getRole().equals(UserRole.ROLE_USER)) {
            if (!review.getAnonymous()) {
                response.setWriter(user.getUserid());
            } else {
                response.setWriter("익명리뷰");
            }

            if (!review.getUser().getId().equals(user.getId())) {
                throw new CNotOwnerException();
            }
        }
//        선생일경우
        if (user.getKinderGarten() != null) {
//            유치원소속과 작성한리뷰가 같아야함
            if (user.getKinderGarten().getId().equals(review.getKinderGarten().getId())) {
                response.setWriter("선생 : " + user.getUserid());
            } else {
                throw new CNotOwnerException();
            }
        }


        ReviewComment reviewComment = new ReviewComment();


        reviewComment.setUser(user);
        reviewComment.setReview(review);
        reviewComment.setDesc(response.getDesc());
        reviewComment.setWriter(response.getWriter());
        ReviewComment save = reviewCommentRepository.save(reviewComment);
        Long id = save.getId();
        return id;
    }

    public CommentDTO.CommentPaging getReviewComments(long reviewid, Pageable pageable) {

        Review review = reviewRepository.findById(reviewid).orElseThrow(CResorceNotfoundException::new);
        Page<ReviewComment> allByReview = reviewCommentRepository.findAllByReview(review, pageable);
        CommentDTO.CommentPaging commentPaging = new CommentDTO.CommentPaging();
        commentPaging.setTotalPage(allByReview.getTotalPages());
        commentPaging.setCurrentpage(allByReview.getPageable().getPageNumber());
        commentPaging.setTotalElements(allByReview.getTotalElements());
        commentPaging.setFindComments(allByReview.getContent());

        return commentPaging;
    }
}
