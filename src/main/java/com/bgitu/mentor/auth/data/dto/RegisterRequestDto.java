package com.bgitu.mentor.auth.data.dto;

import com.bgitu.mentor.auth.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDto {

  @Schema(description = "Почта пользователя", example = "example@gmail.com")
  @Email(message = "Email должен быть правильным")
  @NotBlank(message = "Email не может быть пустым")
  @Size(max = 250, message = "Email слишком длинный")
  private String email;

  @Schema(description = "Пароль пользователя", example = "someStrongPassword")
  @NotBlank(message = "Пароль должен содержать хотя-бы 5 символов")
  @Size(min = 5, message = "Пароль должен содержать хотя-бы 5 символов")
  private String password;

  @Schema(description = "Имя пользователя", example = "Иван")
  @NotBlank(message = "Заполните имя")
  @Size(max = 30, message = "Имя не больше 30 символов")
  private String firstName;

  @Schema(description = "Фамилия пользователя", example = "Иванов")
  @NotBlank(message = "Заполните фамилию")
  @Size(max = 50, message = "Фамилия не больше 50 символов")
  private String lastName;

  @Schema(description = "Роль пользователя", example = "STUDENT")
  private Role role;
}
