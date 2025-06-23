package com.bgitu.mentor.common.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePersonalInfo {

    @Email(message = "Email должен быть корректным")
    private String email;

    private String password;


}
