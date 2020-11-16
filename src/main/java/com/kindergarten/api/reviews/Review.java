package com.kindergarten.api.reviews;

import com.kindergarten.api.kindergartens.KinderGarten;
import com.kindergarten.api.reviews.comment.ReviewComment;
import com.kindergarten.api.users.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "REVIEW")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "accessInfo")
    @Enumerated(EnumType.STRING)
    private AccessInfo accessInfo;

    @NotEmpty//총평
    private String description;
    //    전체만족도
    private double descScore;
    //    시설점수
    private double facilityScore;
    //    선생님점수
    private double teacherScore;
    //    교육점수
    private double eduScore;

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

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ReviewComment> comments = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    private Boolean anonymous;//익명인지 여부


}
