package com.bgitu.mentor.auth.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

  @Schema(description = "Почта пользователя", example = "example@gmail.com")
  @Email(message = "Email должен быть правильным")
  @NotBlank(message = "Email не может быть пустым")
  private String email;

  @Schema(description = "Пароль пользователя", example = "someStrongPassword")
  @Size(min = 5, message = "Пароль должен содержать хотя-бы 5 символов")
  @NotBlank(message = "Пароль не может быть пустым")
  private String password;
}
