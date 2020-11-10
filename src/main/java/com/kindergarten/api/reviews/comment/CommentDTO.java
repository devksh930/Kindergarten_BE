package com.kindergarten.api.reviews.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

public class CommentDTO {

    @Data
    public static class CommentCreate {
        private String writer = "익명리뷰";
        private String desc;
        private Long parentCommentId;
    }

    @Data
    public static class CommentRequest {
        private Long id;
        private Long reviewId;
        private String writer;
        private String desc;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modifiedAt;

        public CommentRequest(ReviewComment reviewComment) {
            this.id = reviewComment.getId();
            this.reviewId = reviewComment.getReview().getId();
            this.writer = reviewComment.getWriter();
            this.desc = reviewComment.getDesc();
            this.createdAt = reviewComment.getCreatedDate();
            this.modifiedAt = reviewComment.getLastModifiedDate();
        }
    }
}
