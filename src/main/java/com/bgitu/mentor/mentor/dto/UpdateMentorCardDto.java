package com.bgitu.mentor.mentor.dto;


import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateMentorCardDto {

    @Size(max = 2000,message = "Сократите описание до 2000 символов")
    private String description;

    @Size(max = 250,message = "Сократите ссылку на ВК до 250 символов")
    private String vkUrl;
    @Size(max = 250,message = "Сократите ссылку на TG до 250 символов")
    private String telegramUrl;

    private Long specialityId;
}
