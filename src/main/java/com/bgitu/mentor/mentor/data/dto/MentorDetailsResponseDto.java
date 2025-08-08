package com.bgitu.mentor.mentor.data.dto;

import com.bgitu.mentor.mentor.data.model.Mentor;
import lombok.Getter;
import lombok.Setter;

/** DTO for {@link Mentor} */
@Getter
@Setter
public class MentorDetailsResponseDto {
  private Long id;
  private String firstName;
  private String lastName;
  private String description;
  private String avatarUrl;
  private String vkUrl;
  private String telegramUrl;
  private String speciality;
  Integer rank;
}
