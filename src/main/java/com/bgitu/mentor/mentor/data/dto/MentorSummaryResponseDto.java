package com.bgitu.mentor.mentor.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentorSummaryResponseDto {
  @Schema(description = "Id ментора", example = "3")
  private Long id;

  @Schema(description = "Имя ментора", example = "Иван")
  private String firstName;

  @Schema(description = "Фамилия ментора", example = "Иванов")
  private String lastName;

  @Schema(description = "Адрес фотографии ментора", example = "/avatar/21.png")
  private String avatarUrl;

  @Schema(description = "Название специальности ментора", example = "Java developer")
  private String specialityName;

  @Schema(description = "Рейтинг ментора", example = "3")
  private Integer rank;

  @Schema(description = "Сокращенное описание ментора", example = "Имею 3 года опыта работы с...")
  private String shortDescription;
}
