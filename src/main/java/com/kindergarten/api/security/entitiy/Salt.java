package com.kindergarten.api.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Salt {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull()
    private String salt;

    public Salt() {

    }

    public Salt(@NotNull() String salt) {
        this.salt = salt;
    }
}
