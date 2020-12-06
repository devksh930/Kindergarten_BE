package com.kindergarten.api.users;

import com.kindergarten.api.kindergartens.KinderGarten;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserid(String userid);

//    User findByUserid(String userid);

    Optional<User> findById(Long id);

    List<User> findByKinderGartenAndRole(KinderGarten kinderGarten, UserRole role);

    Boolean existsByUserid(String userid);

    List<User> findByRole(UserRole role);
}
