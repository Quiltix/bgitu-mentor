package com.bgitu.mentor.student.data.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDetailsResponseDto {

  private Long id;

  private String firstName;

  private String lastName;

  private String description;

  private String avatarUrl;

  private String vkUrl;
  private String telegramUrl;
}
