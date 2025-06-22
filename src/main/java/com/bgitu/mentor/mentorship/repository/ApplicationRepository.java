package com.bgitu.mentor.mentorship.repository;

import com.bgitu.mentor.mentorship.model.Application;
import com.bgitu.mentor.mentorship.model.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByMentorId(Long mentorId);

    List<Application> findByStudentId(Long studentId);

    List<Application> findByMentorIdAndStatus(Long mentorId, ApplicationStatus status);


    boolean existsByStudentIdAndMentorId(Long id, Long id1);
}