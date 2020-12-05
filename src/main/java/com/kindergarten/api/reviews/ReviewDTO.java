package com.kindergarten.api.reviews;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReviewDTO {
//    @Data
//    public static class Create {
//        private AccessInfo accessInfo;
//        private String description;
//        private int facilityScore;
//        private int teacherScore;
//        private int eduScore;
//        private String writer;
//        private String kinderGarten_id;
//        private String user_id;
//    }

    @Data
    public static class CheckResponse {
        private long kindergarten_id;
        private String user_id;
        private String status;
    }

    @Data
    public static class CreateReview {
        @NotNull
        private long kinderGarten_id;
        private String user_id;
        @NotNull
        private String description;
        @NotNull
        private String goodThing;
        @NotNull
        private String badThing;
        private double facilityScore;
        private double teacherScore;
        private double eduScore;
        private double descScore;
        private Boolean anonymous;

        public void setFacilityScore(int facilityScore) {
            this.facilityScore = facilityScore * 2.0;
        }

        public void setTeacherScore(int teacherScore) {
            this.teacherScore = teacherScore * 2.0;
        }

        public void setEduScore(int eduScore) {
            this.eduScore = eduScore * 2.0;
        }

        public void setDescScore(int descScore) {
            this.descScore = descScore * 2.0;
        }

    }

    @Data
    public static class CreateResponse {
        private long reviewindex;
    }

    @Data
    public static class KindergartenReview {
        private int totalPage;
        private int currentpage;
        private long totalElements;
        private double avgScore = 3;


        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage - 1;
        }

        private List<ReviewResponse> findReviews = new ArrayList<>();

        public void setFindReviews(List<Review> reviews) {
            reviews.forEach(review -> {
                ReviewResponse reviewResponse = new ReviewResponse();
                reviewResponse.setAnonymous(review.getAnonymous());
                reviewResponse.setReviewId(review.getId());
                reviewResponse.setAccessInfo(review.getAccessInfo());
                reviewResponse.setWriter(review.getUser().getUserid());
                reviewResponse.setDescription(review.getDescription());
                reviewResponse.setGoodThing(review.getGoodThing());
                reviewResponse.setBadThing(review.getBadThing());
                reviewResponse.setFacilityScore(review.getFacilityScore());
                reviewResponse.setTeacherScore(review.getTeacherScore());
                reviewResponse.setEduScore(review.getEduScore());
                reviewResponse.setDescScore(review.getDescScore());
                reviewResponse.setCreatedDate(review.getCreatedDate());
                this.findReviews.add(reviewResponse);
            });
        }
    }

    @Data
    public static class ReviewResponse {
        private AccessInfo accessInfo; //인증여부
        private Long reviewId;
        private String writer;
        @NotNull
        private String description;
        @NotNull
        private String goodThing;
        @NotNull
        private String badThing;
        private double facilityScore;
        private double teacherScore;
        private double eduScore;
        private double descScore;
        private Boolean anonymous; //익명 여부
        private LocalDateTime createdDate;

        public void setFacilityScore(double facilityScore) {
            this.facilityScore = facilityScore / 2.0;
        }

        public void setTeacherScore(double teacherScore) {
            this.teacherScore = teacherScore / 2.0;
        }

        public void setEduScore(double eduScore) {
            this.eduScore = eduScore / 2.0;
        }

        public void setDescScore(double descScore) {
            this.descScore = descScore / 2.0;
        }


    }
}
