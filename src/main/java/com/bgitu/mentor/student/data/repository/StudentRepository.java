package com.bgitu.mentor.student.data.repository;

import com.bgitu.mentor.student.data.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByEmail(String email);

    List<Student> findAllByMentorId(Long mentorId);
}