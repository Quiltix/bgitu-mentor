package com.bgitu.mentor.mentor.model;

import com.bgitu.mentor.student.model.Student;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "mentor")
public class Mentor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    private String firstName;
    private String lastName;

    private String description;
    private String avatarUrl;
    private String specialty;

    private String vkUrl;
    private String telegramUrl;

    private Integer rank;

    // lazy-связи на остальное
    @ManyToMany(mappedBy = "mentor", fetch = FetchType.LAZY)
    private List<Student> students;

    @OneToMany(mappedBy = "mentor", fetch = FetchType.LAZY)
    private List<Application> requests;

    @OneToMany(mappedBy = "mentor", fetch = FetchType.LAZY)
    private List<Article> articles;

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

}
