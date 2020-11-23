package com.kindergarten.api.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentLogRepository extends JpaRepository<StudentLog, Long> {
    Optional<StudentLog> findByStudents(Student student);
}
