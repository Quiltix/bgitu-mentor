package com.bgitu.mentor.mentorship.data.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationDecisionRequestDto {

  @NotNull(message = "Решение (принять/отклонить) не может быть пустым")
  private Boolean accepted;
}
