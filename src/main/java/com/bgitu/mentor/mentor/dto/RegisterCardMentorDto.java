package com.bgitu.mentor.mentor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;


@Value
@Schema(description = "Данные для заполнения карточки ментора")
public class RegisterCardMentorDto {

    String description;

    @NotBlank(message = "Специальность не должна быть пустой")
    private Long specialityId;

    @NotBlank(message = "Добавьте ссылку на VK")
    String vkUrl;

    String telegramUrl;
}