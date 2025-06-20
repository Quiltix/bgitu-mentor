package com.bgitu.mentor.student.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;




@Getter
@Schema(description = "Данные для заполнения карточки студента")
@Setter
public class RegisterStudentCardDto {

    private String description;

    @NotBlank(message = "Добавьте ссылку на VK")
    private String vkUrl;

    private String telegramUrl;

}
