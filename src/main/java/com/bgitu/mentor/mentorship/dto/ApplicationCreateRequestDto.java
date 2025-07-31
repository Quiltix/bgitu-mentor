package com.bgitu.mentor.mentorship.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ApplicationCreateRequestDto {

    @NotNull
    Long mentorId;

    @NotNull
    @Size(max = 250, message = "Сократите сообщение до 250 символов")
    String message;
}
