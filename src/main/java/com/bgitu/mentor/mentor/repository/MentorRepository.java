package com.bgitu.mentor.mentor.repository;

import com.bgitu.mentor.mentor.model.Mentor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MentorRepository extends JpaRepository<Mentor, Long> {

    boolean existsByEmail(@Email @NotBlank(message = "Email cannot be empty") String email);

    Optional<Mentor> findByEmail(String email);

    List<Mentor> findTop3ByOrderByRankDesc();

    List<Mentor> findAllBySpecialityId(Long specialityId);

    List<Mentor> findBySpecialityIdOrderByRankDesc(Long specId);

    @Query("SELECT m FROM Mentor m " +
            "WHERE LOWER(m.firstName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "   OR LOWER(m.lastName) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "   OR LOWER(m.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Mentor> searchByNameOrDescription(@Param("query") String query);
}