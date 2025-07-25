package com.bgitu.mentor.mentorship.repository;

import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.mentorship.model.Application;
import com.bgitu.mentor.mentorship.model.ApplicationStatus;
import com.bgitu.mentor.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findByMentorId(Long mentorId);

    List<Application> findByStudentId(Long studentId);

    List<Application> findByMentorIdAndStatus(Long mentorId, ApplicationStatus status);

    List<Application> findAllByStudent(Student student);

    boolean existsByStudentAndMentorAndStatus(Student student, Mentor mentor, ApplicationStatus applicationStatus);
}