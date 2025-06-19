package com.bgitu.mentor.student.repository;

import com.bgitu.mentor.student.model.Student;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByEmail(@Email @NotBlank(message = "Email cannot be empty") String email);

    Optional<Student> findByEmail(String email);
}