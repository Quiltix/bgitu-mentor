package com.bgitu.mentor.mentorship.data.repository;

import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentorship.data.model.Application;
import com.bgitu.mentor.mentorship.data.model.ApplicationStatus;
import com.bgitu.mentor.student.data.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {


    List<Application> findAllByStudent(Student student);

    boolean existsByStudentAndMentorAndStatus(Student student, Mentor mentor, ApplicationStatus applicationStatus);

    List<Application> findAllByStudentAndStatus(Student student, ApplicationStatus status);

    List<Application> findAllByMentorAndStatus(Mentor mentor, ApplicationStatus status);

    List<Application> findAllByMentor(Mentor mentor);
}