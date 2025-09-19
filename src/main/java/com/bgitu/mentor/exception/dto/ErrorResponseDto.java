// src/main/java/com/bgitu/mentor/common/dto/ErrorResponseDto.java
package com.bgitu.mentor.exception.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Стандартный ответ об ошибке")
public class ErrorResponseDto {

  @Schema(description = "Время возникновения ошибки", example = "2023-12-01T15:30:45.123")
  private final LocalDateTime timestamp;

  @Schema(description = "HTTP статус код", example = "***")
  private final int status;

  @Schema(description = "Краткое описание статуса", example = "Something Went Wrong")
  private final String error;

  @Schema(description = "Сообщение для пользователя", example = "Для пользователя")
  private final String message;

  @Schema(description = "Путь API где произошла ошибка", example = "/api/something")
  private final String path;

  @Schema(
      description = "Детали ошибок валидации (только для статуса 400)",
      example = "{\"title\": \"Название не может быть пустым\"}")
  private Map<String, String> validationErrors;

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
