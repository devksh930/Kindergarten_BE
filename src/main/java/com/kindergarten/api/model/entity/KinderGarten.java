package com.kindergarten.api.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "KINDERGARTEN")
public class KinderGarten {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    //    설립유형
    @NotNull
    private String type;

    //     설립일
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDateTime openDate;

    private String address;

    private String phone;

    private String website;

    private String operatingTime;


}
