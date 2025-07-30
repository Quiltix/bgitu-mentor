package com.bgitu.mentor.vote.data.model;

import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.student.model.Student;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "mentor_vote", uniqueConstraints = @UniqueConstraint(columnNames = {"mentor_id", "user_id"}))
@Getter
@Setter
@Entity
public class MentorVote extends BaseVote {


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;


}

