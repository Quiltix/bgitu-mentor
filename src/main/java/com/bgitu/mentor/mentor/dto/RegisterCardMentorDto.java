package com.bgitu.mentor.mentor.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;


@Value
public class RegisterCardMentorDto {

    String description;

    @NotBlank(message = "Специальность не должна быть пустой")
    String speciality;

    @NotBlank(message = "Добавьте ссылку на VK")
    String vkUrl;

    String telegramUrl;
}