package com.kindergarten.api.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kindergarten.api.kindergartens.KinderGarten;
import com.kindergarten.api.users.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "STUDENT")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate birthday;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "KINDERGARTENT_ID")
    private KinderGarten kinderGarten;

    //    승인확인
    @Column
    private Boolean access = false;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "students")
    private List<StudentLog> studentLogs;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDate createdDate = LocalDate.now();

}
