package com.bgitu.mentor.mentor.data.repository;

import com.bgitu.mentor.mentor.data.model.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MentorRepository
    extends JpaRepository<Mentor, Long>, JpaSpecificationExecutor<Mentor> {

  List<Mentor> findTop3ByOrderByRankDesc();
}
