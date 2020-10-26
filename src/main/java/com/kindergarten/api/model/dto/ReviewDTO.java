package com.kindergarten.api.model.dto;

import com.kindergarten.api.model.entity.CurrentStudent;
import lombok.Data;

public class ReviewDTO {
    @Data
    public static class Create {
        private CurrentStudent currentStudent;
        private String description;
        private int facilityScore;
        private int teacherScore;
        private int eduScore;
        private String writer;
        private String kinderGarten_id;
        private String user_id;
    }

    @Data
    public static class response {
        private CurrentStudent currentStudent;
        private String description;
        private int facilityScore;
        private int teacherScore;
        private int eduScore;
        private String writer;
        private String kinderGarten_id;
        private String user_id;
    }
}
