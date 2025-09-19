package com.bgitu.mentor.mentorship.data.dto;

import com.bgitu.mentor.mentorship.data.model.ApplicationStatus;
import com.bgitu.mentor.student.data.dto.StudentDetailsResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationDetailsResponseDto {

  @Schema(description = "Id заявки", example = "3")
  Long id;

  @Schema(
      description = "Сообщение заявки от студента",
      example = "Возьмите пожалуйста на менторство")
  @Size(max = 250, message = "Сократите сообщение до 250 символов")
  String message;

  @Schema(
      description =
          """
                      Статус заявки(  PENDING,
                        ACCEPTED,
                        REJECTED,
                        EXPIRED,
                        CANCELED)""",
      example = "PENDING")
  ApplicationStatus status;

  @Schema(description = "Студент", implementation = StudentDetailsResponseDto.class)
  StudentDetailsResponseDto student;
}
