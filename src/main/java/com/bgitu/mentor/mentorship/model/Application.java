package com.bgitu.mentor.mentorship.model;

import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.student.model.Student;
import jakarta.persistence.*;

@Entity
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus status; // PENDING, ACCEPTED, REJECTED
}
