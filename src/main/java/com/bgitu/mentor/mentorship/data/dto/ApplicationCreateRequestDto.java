package com.bgitu.mentor.mentorship.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationCreateRequestDto {

  @Schema(description = "Id ментора", example = "1")
  @NotNull
  Long mentorId;

  @Schema(description = "Сообщение ментору ", example = "Хочу обучаться")
  @NotNull
  @Size(max = 250, message = "Сократите сообщение до 250 символов")
  String message;
}
