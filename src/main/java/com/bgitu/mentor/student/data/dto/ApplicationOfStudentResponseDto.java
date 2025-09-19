package com.bgitu.mentor.student.data.dto;

import com.bgitu.mentor.mentorship.data.model.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationOfStudentResponseDto {
  @Schema(description = "Id заявки", example = "3")
  private Long id;

  @Schema(
      description = "Сообщение заявки от студента",
      example = "Возьмите пожалуйста на менторство")
  private String message;

  @Schema(description = "Имя и Фамилия через пробел", example = "Иван Иванов")
  private String mentorFullName;

  @Schema(description = "Название специальности ментора", example = "Backend developer")
  private String mentorSpeciality;

  @Schema(
      description = "Статус заявки(  PENDING, ACCEPTED, REJECTED, EXPIRED, CANCELED)",
      example = "PENDING")
  private ApplicationStatus status;
}
