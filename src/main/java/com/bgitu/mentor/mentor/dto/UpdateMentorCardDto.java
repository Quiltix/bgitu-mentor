package com.bgitu.mentor.mentor.dto;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateMentorCardDto {
    private String description;
    private String vkUrl;
    private String telegramUrl;
    private Long specialityId;
}
