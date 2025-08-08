package com.bgitu.mentor.mentor.data.repository;

import com.bgitu.mentor.mentor.data.model.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MentorRepository
    extends JpaRepository<Mentor, Long>, JpaSpecificationExecutor<Mentor> {}
