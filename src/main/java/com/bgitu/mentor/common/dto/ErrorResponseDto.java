// src/main/java/com/bgitu/mentor/common/dto/ErrorResponseDto.java
package com.bgitu.mentor.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // Не включать в JSON поля, которые равны null
public class ErrorResponseDto {

  private final LocalDateTime timestamp;
  private final int status;
  private final String error; // Краткое описание статуса, e.g., "Bad Request"
  private final String message; // Сообщение для пользователя
  private final String path; // Путь, на котором произошла ошибка
  private Map<String, String> validationErrors; // Для ошибок валидации

  public ErrorResponseDto(HttpStatus httpStatus, String message, WebRequest request) {
    this.timestamp = LocalDateTime.now();
    this.status = httpStatus.value();
    this.error = httpStatus.getReasonPhrase();
    this.message = message;
    this.path = request.getDescription(false).replace("uri=", "");
  }

  public ErrorResponseDto(
      HttpStatus httpStatus,
      String message,
      WebRequest request,
      Map<String, String> validationErrors) {
    this(httpStatus, message, request);
    this.validationErrors = validationErrors;
  }
}
