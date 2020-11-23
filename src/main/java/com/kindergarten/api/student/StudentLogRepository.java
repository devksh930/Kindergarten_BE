package com.kindergarten.api.student;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentLogRepository extends JpaRepository<StudentLog, Long> {
    Optional<StudentLog> findByStudents(Student student);
}
