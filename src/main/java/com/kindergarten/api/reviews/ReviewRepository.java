package com.kindergarten.api.reviews;

import com.kindergarten.api.kindergartens.KinderGarten;
import com.kindergarten.api.users.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByUserAndKinderGarten(User user, KinderGarten kinderGarten);

    Page<Review> findByKinderGarten(KinderGarten kinderGarten, Pageable pageable);

}
