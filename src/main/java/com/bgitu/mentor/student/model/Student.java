package com.bgitu.mentor.student.model;

import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.mentorship.model.Application;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String firstName;

    private String lastName;

    private String description;//

    private String avatarUrl;//

    private String vkUrl;//
    private String telegramUrl;//

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id")  // внешний ключ
    private Mentor mentor;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<Application> applications;
}