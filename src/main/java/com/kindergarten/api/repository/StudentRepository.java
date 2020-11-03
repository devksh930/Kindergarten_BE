package com.kindergarten.api.repository;

import com.kindergarten.api.model.entity.KinderGarten;
import com.kindergarten.api.model.entity.Student;
import com.kindergarten.api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByUser(User user);

    boolean existsByKinderGarten(KinderGarten kinderGarten);

    boolean existsByUserAndAccessTrue(User user);


    List<Student> findByUserAndKinderGarten(User user, KinderGarten kinderGarten);

    List<Student> findByKinderGarten(KinderGarten kinderGarten);

    List<Student> findByUserAndKinderGartenAndAccessTrue(User user, KinderGarten kinderGarten);


}
