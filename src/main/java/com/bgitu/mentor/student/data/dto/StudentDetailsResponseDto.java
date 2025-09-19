package com.bgitu.mentor.student.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDetailsResponseDto {
  @Schema(
      description =
          """
                              Статус заявки(  PENDING,
                                ACCEPTED,
                                REJECTED,
                                EXPIRED,
                                CANCELED)""",
      example = "3")
  private Long id;

  private String firstName;

  private String lastName;

  private String description;

  private String avatarUrl;

  private String vkUrl;
  private String telegramUrl;
}
