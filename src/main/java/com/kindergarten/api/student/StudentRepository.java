package com.kindergarten.api.student;

import com.kindergarten.api.kindergartens.KinderGarten;
import com.kindergarten.api.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByUser(User user);

    boolean existsByKinderGarten(KinderGarten kinderGarten);

    boolean existsByUserAndKinderGarten(User user, KinderGarten kinderGarten);

    boolean existsByUserAndKinderGartenAndAccessTrue(User user, KinderGarten kinderGarten);

    boolean existsByUserAndAccessTrue(User user);

    List<Student> findByUser(User user);

    List<Student> findByKinderGarten(KinderGarten kinderGarten);

    List<Student> findByUserAndKinderGartenAndAccessTrue(User user, KinderGarten kinderGarten);


}
