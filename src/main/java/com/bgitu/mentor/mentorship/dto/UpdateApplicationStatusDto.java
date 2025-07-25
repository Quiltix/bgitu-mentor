package com.bgitu.mentor.mentorship.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateApplicationStatusDto {

    @NotNull(message = "Решение (принять/отклонить) не может быть пустым")
    private Boolean accepted;

    // Поле для будущего расширения
    //private String reason;
}
