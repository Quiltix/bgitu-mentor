package com.bgitu.mentor.common.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCredentialsUpdateRequestDto {

    @Email(message = "Email должен быть корректным")
    private String email;

    private String password;


}
