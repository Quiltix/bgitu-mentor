package com.bgitu.mentor.mentor.data.model;

import com.bgitu.mentor.article.model.Article;
import com.bgitu.mentor.mentorship.model.Application;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.user.model.BaseUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "mentor")
public class Mentor extends BaseUser {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speciality_id")
    private Speciality speciality;

    private Integer rank = 0;

    @OneToMany(mappedBy = "mentor", fetch = FetchType.LAZY)
    private List<MentorVote> votes;

    @OneToMany(mappedBy = "mentor", fetch = FetchType.LAZY)
    private List<Student> students;

    @OneToMany(mappedBy = "mentor", fetch = FetchType.LAZY)
    private List<Application> applications;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private List<Article> articles;


}
