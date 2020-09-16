package com.kindergarten.api.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@Table(name = "teacher")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String userid;

    @NotNull
    private String password;

    @NotNull
    private String name;

    @NotNull
    private String phone;

    @NotNull
    @Email
    private String email;

    @ManyToOne
    @JoinColumn(name = "KINDERGATENT_ID")
    private KinderGarten kinderGarten;

}
