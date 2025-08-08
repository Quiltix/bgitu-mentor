package com.bgitu.mentor.mentor.data.model;

import com.bgitu.mentor.speciality.data.model.Speciality;
import com.bgitu.mentor.user.data.model.BaseUser;
import com.bgitu.mentor.vote.data.model.Votable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "mentor")
public class Mentor extends BaseUser implements Votable {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "speciality_id")
  private Speciality speciality;

  private Integer rank = 0;

  @Override
  public Integer getRank() {
    return rank;
  }
}
