package com.bgitu.mentor.mentorship.model;

import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.student.model.Student;
import jakarta.persistence.*;

@Entity
public class MentorApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @ManyToOne
    private Student student;

    @ManyToOne
    private Mentor mentor;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status; // PENDING, ACCEPTED, REJECTED
}
