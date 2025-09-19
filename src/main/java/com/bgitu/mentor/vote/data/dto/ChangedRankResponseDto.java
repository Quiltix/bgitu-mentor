package com.bgitu.mentor.vote.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ChangedRankResponseDto {
  @Schema(description = "Новый ранг", example = "3")
  private Integer rank;
}
