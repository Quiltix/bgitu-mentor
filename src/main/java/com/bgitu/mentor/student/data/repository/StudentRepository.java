package com.bgitu.mentor.student.data.repository;

import com.bgitu.mentor.student.data.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {



    List<Student> findAllByMentorId(Long mentorId);
}