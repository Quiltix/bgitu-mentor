package com.bgitu.mentor.mentor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
public class UpdateMentorCardDto {
    private String description;
    private String vkUrl;
    private String telegramUrl;
    private Long specialityId;
}
