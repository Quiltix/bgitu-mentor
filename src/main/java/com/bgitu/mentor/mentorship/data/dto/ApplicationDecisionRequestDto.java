package com.bgitu.mentor.mentorship.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationDecisionRequestDto {

  @Schema(description = "Решение ментора по заявке", example = "true")
  @NotNull(message = "Решение (принять/отклонить) не может быть пустым")
  private Boolean accepted;
}
