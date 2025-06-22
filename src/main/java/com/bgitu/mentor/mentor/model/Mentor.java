package com.bgitu.mentor.mentor.model;

import com.bgitu.mentor.article.model.Article;
import com.bgitu.mentor.mentorship.model.Application;
import com.bgitu.mentor.student.model.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

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

    @Column(length = 2000)
    private String description;
    private String avatarUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speciality_id")
    private Speciality speciality;



    private String vkUrl;
    private String telegramUrl;

    private Integer rank;

    @OneToMany(mappedBy = "mentor", fetch = FetchType.LAZY)
    private List<Student> students;

    @OneToMany(mappedBy = "mentor", fetch = FetchType.LAZY)
    private List<Application> applications;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Article> articles;


}
