package com.bgitu.mentor.mentor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateMentorProfileDto {

    @Email(message = "Email должен быть корректным")
    private String email;

    @Size(min = 5, message = "Пароль должен содержать минимум 5 символов")
    private String password;

    private String firstName;

    private String lastName;
}
