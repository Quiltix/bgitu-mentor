package com.bgitu.mentor.mentorship.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentPreviewDto {
    Long id;
    String firstName;
    String lastName;
    String description;
    String avatarUrl;
    String vkUrl;
    String telegramUrl;

    public StudentPreviewDto(Long id, String firstName, String lastName, String description, String avatarUrl, String vkUrl, String telegramUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
        this.avatarUrl = avatarUrl;
        this.vkUrl = vkUrl;
        this.telegramUrl = telegramUrl;
    }
}