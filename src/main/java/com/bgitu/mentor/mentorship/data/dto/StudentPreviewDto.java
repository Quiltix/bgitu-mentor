package com.bgitu.mentor.mentorship.data.dto;


import com.bgitu.mentor.student.data.model.Student;
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

    public StudentPreviewDto(Student student) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.description = student.getDescription();
        this.avatarUrl = student.getAvatarUrl();
        this.vkUrl = student.getVkUrl();
        this.telegramUrl = student.getTelegramUrl();
    }
}