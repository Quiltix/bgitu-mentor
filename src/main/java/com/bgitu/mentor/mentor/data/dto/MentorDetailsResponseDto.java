package com.bgitu.mentor.mentor.data.dto;

import com.bgitu.mentor.mentor.data.model.Mentor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/** DTO for {@link Mentor} */
@Getter
@Setter
public class MentorDetailsResponseDto {
  @Schema(description = "Id ментора", example = "3")
  private Long id;

  @Schema(description = "Имя ментора", example = "Иван")
  private String firstName;

  @Schema(description = "Фамилия ментора", example = "Иванов")
  private String lastName;

  @Schema(description = "Описание ментора", example = "Имею 3 года опыта работы с...")
  private String description;

  @Schema(description = "Адрес фотографии ментора", example = "/avatar/21.png")
  private String avatarUrl;

  @Schema(description = "Ссылка на VK", example = "https://vk.com/profsoyuz_bgitu")
  private String vkUrl;

  @Schema(description = "Ссылка на TG", example = "https://t.me/quiltix")
  private String telegramUrl;

  @Schema(description = "Название специальности ментора", example = "Java developer")
  private String specialityName;

  @Schema(description = "Рейтинг ментора", example = "3")
  Integer rank;

  @Schema(
      description = "Отображает возможность для текущего пользователя проголосовать за ментора ",
      example = "true")
  private Boolean canVote;
}
