package com.kindergarten.api.repository;

import com.kindergarten.api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserid(String userid);

//    User findByUserid(String userid);

    Optional<User> findById(Long id);

    Boolean existsByUserid(String userid);

}
