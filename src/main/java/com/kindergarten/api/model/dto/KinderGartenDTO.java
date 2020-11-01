package com.kindergarten.api.model.dto;

import com.kindergarten.api.model.entity.KinderGarten;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class KinderGartenDTO {
    //    회원가입시 사용하는 DTO
    @Data
    public static class KindergatenPage {
        private int totalPage;
        private int currentpage;

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage - 1;
        }

        private List<find_KinderGarten> kinderGartens = new ArrayList<>();

        public void setKinderGartens(List<KinderGarten> kinderGartens) {
            kinderGartens.forEach(kinderGarten -> {
                Long id = kinderGarten.getId();
                String name = kinderGarten.getName();
                String address = kinderGarten.getAddress();
                String type = kinderGarten.getType();
                Double score = kinderGarten.getScore();
                Boolean kinder = kinderGarten.getIsKinder();
                find_KinderGarten find_kinderGarten = new find_KinderGarten();
                String iskinder = null;
                find_kinderGarten.setId(id);
                find_kinderGarten.setName(name);
                find_kinderGarten.setAddress(address);
                find_kinderGarten.setType(type);
                find_kinderGarten.setScore(score);
                if (kinder) {
                    iskinder = "유치원";
                } else if (!kinder) {
                    iskinder = "어린이집";
                }
                find_kinderGarten.setKinder_type(iskinder);
                this.kinderGartens.add(find_kinderGarten);
            });
        }
    }

    @Data
    public static class find_KinderGarten {
        private Long id;
        private String name;
        private String address;
        private String type;
        private Double score;
        private String kinder_type;
    }

    @Data
    public static class KinderGartenDetail {
        private Long id;
        private String name;
        private String type;
        private LocalDate openDate;
        private String address;
        private String phone;
        private String website;
        private String operatingTime;
        private double score;
    }
}
