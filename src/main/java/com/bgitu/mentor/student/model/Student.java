package com.bgitu.mentor.student.model;

import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentorship.model.Application;
import com.bgitu.mentor.user.model.BaseUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "student")
public class Student extends BaseUser {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<Application> applications;
}