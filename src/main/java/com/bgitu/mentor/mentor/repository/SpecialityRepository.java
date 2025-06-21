package com.bgitu.mentor.mentor.repository;

import com.bgitu.mentor.mentor.model.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
    boolean existsByName(String name);

    Optional<Speciality> getSpecialityById(Long id);
}
