package com.kindergarten.api.repository;

import com.kindergarten.api.model.entity.KinderGarten;
import com.kindergarten.api.model.entity.Review;
import com.kindergarten.api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByUserAndKinderGarten(User user, KinderGarten kinderGarten);
}
