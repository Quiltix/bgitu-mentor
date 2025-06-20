package com.bgitu.mentor.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    @Email(message = "Email должен быть правильным")
    @NotBlank(message = "Email не может быть пустым")
    private String email;

    @Size(min = 5, message = "Пароль должен содержать хотя-бы 5 символов")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
}
