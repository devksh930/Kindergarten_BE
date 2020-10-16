package com.kindergarten.api.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "STUDENTLOG")
public class StudentLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    다니기 시작했던 날짜
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate startDate;
    //    퇴원을 했던 날짜
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate endDate;
    //  승인이 되어 있었는지 확인
    private boolean access;

    @ManyToOne
    @JoinColumn(name = "KINDERGARTEN_ID")
    private KinderGarten kinderGarten;

    @ManyToOne
    @JoinColumn(name = "STUDENT_ID")
    private Student students;
}