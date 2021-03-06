package com.kindergarten.api.kindergartens;

import com.kindergarten.api.reviews.Review;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private LocalDate openDate;

    private String address;

    private String phone;

    private String website;

    private String operatingTime;
    @OneToMany(mappedBy = "kinderGarten")
    private List<Review> reviews = new ArrayList<>();

    @ColumnDefault("10.0")
    private Double score;

    public Double getScore() {
        return score / 2.0;
    }

    public Boolean isKinder;
}
