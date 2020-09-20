package com.kindergarten.api.repository;

import com.kindergarten.api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<User, Long> {
}
