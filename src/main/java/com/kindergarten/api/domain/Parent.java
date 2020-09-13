package com.kindergarten.api.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@Table(name = "parent")
public class Parent {
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
}
