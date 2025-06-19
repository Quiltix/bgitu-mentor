package com.bgitu.mentor.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    @Email
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @Size(min = 5, message = "Password must be at least 5 characters long")
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
