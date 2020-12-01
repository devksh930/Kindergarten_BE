package com.kindergarten.api.reviews.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommentDTO {

    @Data
    public static class CommentCreate {
        private String writer = "익명리뷰";
        private String desc;
    }

    @Data
    public static class CommentResponse {
        private Long id;
        private Long reviewId;
        private String writer;
        private String desc;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        private LocalDateTime modifiedAt;
        private String userid;

    }

    @Data
    public static class CommentPaging {
        private int totalPage;
        private int currentpage;
        private long totalElements;
        private List<CommentResponse> findComments = new ArrayList<>();

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage - 1;
        }

        public void setFindComments(List<ReviewComment> comments) {
            comments.forEach(comment -> {
                CommentResponse commentResponse = new CommentResponse();
                commentResponse.setId(comment.getId());
                commentResponse.setReviewId(comment.getReview().getId());
                commentResponse.setWriter(comment.getWriter());
                commentResponse.setDesc(comment.getDesc());
                commentResponse.setCreatedAt(comment.getCreatedDate());
                commentResponse.setModifiedAt(comment.getLastModifiedDate());
                commentResponse.setUserid(comment.getUser().getUserid());
                this.findComments.add(commentResponse);
            });
        }
    }

    @Data
    public static class CommentModify {
        private String desc;
    }
}
