package com.kindergarten.api.student;

import com.kindergarten.api.kindergartens.KinderGarten;
import com.kindergarten.api.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {


    boolean existsByUserAndKinderGarten(User user, KinderGarten kinderGarten);

    boolean existsByUserAndKinderGartenAndAccessTrue(User user, KinderGarten kinderGarten);

    List<Student> findByKinderGarten(KinderGarten kinderGarten);



}
