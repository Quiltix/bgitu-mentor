package com.bgitu.mentor.mentorship.data.dto;

import com.bgitu.mentor.mentorship.data.model.ApplicationStatus;
import com.bgitu.mentor.student.data.dto.StudentDetailsResponseDto;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationDetailsResponseDto {

  Long id;

  @Size(max = 250, message = "Сократите сообщение до 250 символов")
  String message;

  ApplicationStatus status;

  StudentDetailsResponseDto student;
}
