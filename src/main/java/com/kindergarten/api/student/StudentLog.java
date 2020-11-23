package com.kindergarten.api.student;

import com.kindergarten.api.kindergartens.KinderGarten;
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

    //    학생등록 날짜
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate startDate;
    //    학생등록을변경 했던 날짜
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate endDate = LocalDate.now();
    //  승인이 되어 있었는지 확인
    private boolean access;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KINDERGARTEN_ID")
    private KinderGarten kinderGarten;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID")
    private Student students;
}