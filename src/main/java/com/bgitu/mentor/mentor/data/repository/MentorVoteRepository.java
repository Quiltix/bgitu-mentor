package com.bgitu.mentor.mentor.data.repository;

import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.model.MentorVote;
import com.bgitu.mentor.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MentorVoteRepository extends JpaRepository<MentorVote, Long> {
    boolean existsByMentorAndStudent(Mentor mentor, Student student);
}
