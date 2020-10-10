package com.kindergarten.api.model.dto;

import com.kindergarten.api.model.entity.KinderGarten;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class KinderGartenDTO {
    //    회원가입시 사용하는 DTO
    @Data
    public static class UserCreate {
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
                find_KinderGarten find_kinderGarten = new find_KinderGarten();

                find_kinderGarten.setId(id);
                find_kinderGarten.setName(name);
                find_kinderGarten.setAddress(address);
                find_kinderGarten.setType(type);
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
    }
    @Data
    public static class  KinderGartenDetail{
        private Long id;
        private String name;
        private String type;
        private LocalDate openDate;
        private String address;
        private String phone;
        private String website;
        private String operatingTime;

    }
}
