package com.kindergarten.api.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDateTime birthday;

    @ManyToOne
    @JoinColumn(name = "KINDERGATENT_ID")
    private KinderGarten kinderGarten;

    //    승인확인
    private boolean access;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
}
