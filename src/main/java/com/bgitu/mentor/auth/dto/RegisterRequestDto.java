package com.bgitu.mentor.auth.dto;


import com.bgitu.mentor.auth.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDto {

    @Email
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank
    @Size(min = 5,message = "Password must be at least 5 characters")
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private Role role;
}
