package com.kindergarten.api.security.repository;

import com.kindergarten.api.security.entitiy.Salt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaltRepository extends JpaRepository<Salt, Long> {
}
