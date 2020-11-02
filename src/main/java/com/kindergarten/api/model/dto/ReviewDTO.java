package com.kindergarten.api.model.dto;

import com.kindergarten.api.model.entity.AccessInfo;
import lombok.Data;

public class ReviewDTO {
    @Data
    public static class Create {
        private AccessInfo accessInfo;
        private String description;
        private int facilityScore;
        private int teacherScore;
        private int eduScore;
        private String writer;
        private String kinderGarten_id;
        private String user_id;
    }

    @Data
    public static class CheckResponse {
        private String kindergarten_id;
        private String user_id;
        private String status;
    }

    @Data
    public static class response {
        private AccessInfo accessInfo;
        private String description;
        private int facilityScore;
        private int teacherScore;
        private int eduScore;
        private String writer;
        private String kinderGarten_id;
        private String user_id;
    }
}
