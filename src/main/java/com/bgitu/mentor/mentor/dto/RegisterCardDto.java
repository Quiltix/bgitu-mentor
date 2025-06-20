package com.bgitu.mentor.mentor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

/**
 * DTO for {@link com.bgitu.mentor.mentor.model.Mentor}
 */
@Value
public class RegisterCardDto {

    String description;

    @NotBlank
    String specialty;

    @NotBlank
    String vkUrl;

    String telegramUrl;

}