package com.bgitu.mentor.user.data.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCredentialsResponseDto {

  @Email(message = "Email должен быть корректным")
  private String email;
}
