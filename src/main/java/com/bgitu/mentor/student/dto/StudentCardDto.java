package com.bgitu.mentor.student.dto;

import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.mentorship.model.Application;
import com.bgitu.mentor.student.model.Student;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudentCardDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String description;

    private String avatarUrl;

    private String vkUrl;
    private String telegramUrl;

    public StudentCardDto(Student student) {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.description = student.getDescription();
        this.avatarUrl = student.getAvatarUrl();
        this.vkUrl = student.getVkUrl();
        this.telegramUrl = student.getTelegramUrl();
    }
}
