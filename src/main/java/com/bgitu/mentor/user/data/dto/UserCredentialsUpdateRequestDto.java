package com.bgitu.mentor.user.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCredentialsUpdateRequestDto {

  @Schema(description = "Почта пользователя", example = "example@domain.com")
  @Email(message = "Email должен быть корректным")
  private String email;

  @Schema(description = "Новый пароль", example = "SomeStrongPassword")
  private String password;
}
