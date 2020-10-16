package com.kindergarten.api.repository;

import com.kindergarten.api.model.entity.KinderGarten;
import com.kindergarten.api.model.entity.Student;
import com.kindergarten.api.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByUser(User user);

    List<Student> findByKinderGarten(KinderGarten kinderGarten);
}
