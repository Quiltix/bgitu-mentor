package com.bgitu.mentor.mentor.repository;

import com.bgitu.mentor.mentor.model.Mentor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MentorRepository extends JpaRepository<Mentor, Long> {

    boolean existsByEmail(@Email @NotBlank(message = "Email cannot be empty") String email);

    Optional<Mentor> findByEmail(String email);

    List<Mentor> findTop3ByOrderByRankDesc();

    List<Mentor> findAllBySpecialityId(Long specialityId);
}