package com.bgitu.mentor.user.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseUserUpdateRequestDto {

  @Schema(description = "Имя пользователя", example = "Иван")
  @Size(max = 30, message = "Имя не больше 30 символов")
  private String firstName;

  @Schema(description = "Фамилия пользователя", example = "Иванов")
  @Size(max = 50, message = "Фамилия не больше 50 символов")
  private String lastName;

  @Schema(
      description = "Описание пользователя(опыт, скилы)",
      example = "Имею 3 года опыта работы с...")
  @Size(max = 2000, message = "Сократите описание до 2000 символов")
  private String description;

  @Schema(description = "Ссылка на личный вк", example = "https://vk.com/profsoyuz_bgitu")
  @Size(max = 250, message = "Сократите ссылку на ВК до 250 символов")
  private String vkUrl;

  @Schema(description = "Ссылка на личный tg", example = "https://t.me/quiltix")
  @Size(max = 250, message = "Сократите ссылку на TG до 250 символов")
  private String telegramUrl;
}
