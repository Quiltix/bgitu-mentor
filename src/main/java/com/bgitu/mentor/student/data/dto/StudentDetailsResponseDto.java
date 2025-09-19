package com.bgitu.mentor.student.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDetailsResponseDto {

  @Schema(description = "Id студента", example = "1")
  private Long id;

  @Schema(description = "Имя студента", example = "Иван")
  private String firstName;

  @Schema(description = "Фамилия студента", example = "Иванов")
  private String lastName;

  @Schema(description = "Описание студента", example = "Я студент 3 курса")
  private String description;

  @Schema(description = "Ссылка на аватар студента", example = "avatar/avatar.jpg")
  private String avatarUrl;

  @Schema(description = "Ссылка на профиль в ВК", example = "https://vk.com/ivanov")
  private String vkUrl;

  @Schema(description = "Ссылка на профиль в Telegram", example = "https://t.me/ivanov")
  private String telegramUrl;
}
