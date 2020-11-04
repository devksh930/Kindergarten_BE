package com.kindergarten.api.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "REVIEW")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "accessInfo")
    @Enumerated(EnumType.STRING)
    private AccessInfo accessInfo;

    @NotEmpty
    private String description;
    //    전체만족도
    private int descScore;
    //    시설점수
    private int facilityScore;
    //    선생님점수
    private int teacherScore;
    //    교육점수
    private int eduScore;

    @NotNull
    private String goodThing;
    @NotNull
    private String badThing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KINDERGARTEN_ID")
    private KinderGarten kinderGarten;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    private Boolean anonymous;//익명인지 여부
}
