package com.bgitu.mentor.student.data.model;

import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.user.data.model.BaseUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "student")
public class Student extends BaseUser {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "mentor_id")
  private Mentor mentor;
}
