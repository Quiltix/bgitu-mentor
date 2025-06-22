package com.bgitu.mentor.auth.dto;


import com.bgitu.mentor.auth.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDto {

    @Email(message = "Email должен быть правильным")
    @NotBlank(message = "Email не может быть пустым")
    @Size(max = 250,message = "Email слишком длинный")
    private String email;

    @NotBlank(message = "Пароль должен содержать хотя-бы 5 символов")
    @Size(min = 5,message = "Пароль должен содержать хотя-бы 5 символов")
    private String password;

    @NotBlank(message = "Заполните имя")
    @Size(max = 30,message = "Имя не больше 30 символов")
    private String firstName;

    @NotBlank(message = "Заполните фамилию")
    @Size(max = 50,message = "Фамилия не больше 50 символов")
    private String lastName;

    private Role role;
}
