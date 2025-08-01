package com.bgitu.mentor.mentor.data.repository;

import com.bgitu.mentor.mentor.data.model.Mentor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import java.util.Optional;

public interface MentorRepository extends JpaRepository<Mentor, Long>, JpaSpecificationExecutor<Mentor> {

    boolean existsByEmail(@Email @NotBlank(message = "Email cannot be empty") String email);

    Optional<Mentor> findByEmail(String email);


}