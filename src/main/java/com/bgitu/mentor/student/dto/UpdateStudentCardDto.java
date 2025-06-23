package com.bgitu.mentor.student.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStudentCardDto {

    @Size(max = 30,message = "Имя не больше 30 символов")
    private String firstName;

    @Size(max = 50,message = "Фамилия не больше 50 символов")
    private String lastName;

    @Size(max = 2000,message = "Сократите описание до 2000 символов")
    private String description;

    @Size(max = 250,message = "Сократите ссылку на ВК до 250 символов")
    private String vkUrl;
    @Size(max = 250,message = "Сократите ссылку на TG до 250 символов")
    private String telegramUrl;
}
