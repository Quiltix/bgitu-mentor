package com.bgitu.mentor.mentor.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentorSummaryResponseDto {
  private Long id;
  private String firstName;
  private String lastName;
  private String avatarUrl;
  private String specialityName;
  private Integer rank;
  private String shortDescription;
}
