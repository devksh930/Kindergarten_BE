package com.kindergarten.api.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

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
        private int facilityScore;
        private int teacherScore;
        private int eduScore;
        private int descScore;
        private Boolean anonymous;

        public void setFacilityScore(int facilityScore) {
            this.facilityScore = facilityScore * 2;
        }

        public void setTeacherScore(int teacherScore) {
            this.teacherScore = teacherScore * 2;
        }

        public void setEduScore(int eduScore) {
            this.eduScore = eduScore * 2;
        }

        public void setDescScore(int descScore) {
            this.descScore = descScore * 2;
        }

    }

    @Data
    public static class CreateResponse {
        private long reviewindex;
    }
}
