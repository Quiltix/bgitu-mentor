package com.bgitu.mentor.mentorship.data.repository;


import com.bgitu.mentor.mentorship.data.model.Application;
import com.bgitu.mentor.mentorship.data.model.ApplicationStatus;
import com.bgitu.mentor.student.data.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findAllByMentorIdAndStatus(Long mentorId, ApplicationStatus status);
    List<Application> findAllByMentorId(Long mentorId);

    List<Application> findAllByStudentIdAndStatus(Long studentId, ApplicationStatus status);
    List<Application> findAllByStudentId(Long studentId);

    boolean existsByStudentIdAndMentorIdAndStatus(Long studentId, Long mentorId, ApplicationStatus status);

    List<Application> findAllByStudentAndStatus(Student student, ApplicationStatus status);
}