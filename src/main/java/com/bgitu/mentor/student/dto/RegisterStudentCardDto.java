package com.bgitu.mentor.student.dto;

import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.mentorship.model.Application;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class RegisterStudentCardDto {

    private String description;

    @NotBlank(message = "Добавьте ссылку на VK")
    private String vkUrl;

    private String telegramUrl;

}
