package com.kindergarten.api.model.dto;

import com.kindergarten.api.model.entity.KinderGarten;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KinderGartenDTO {
    private Long id;
    private String name;
    private String type;
    private LocalDateTime openDate;
    private String address;
    private String phone;
    private String website;
    private String operatingTime;

    public KinderGartenDTO() {
    }

    public KinderGartenDTO(KinderGarten kinderGarten) {
        this.id = kinderGarten.getId();
        this.name = kinderGarten.getName();
        this.type = kinderGarten.getType();
        this.openDate = kinderGarten.getOpenDate();
        this.address = kinderGarten.getAddress();
        this.phone = kinderGarten.getPhone();
        this.website = kinderGarten.getWebsite();
        this.operatingTime = kinderGarten.getOperatingTime();
    }
}
