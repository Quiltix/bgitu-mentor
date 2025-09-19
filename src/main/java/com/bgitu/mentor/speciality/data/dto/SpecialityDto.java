package com.bgitu.mentor.speciality.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SpecialityDto {
  @Schema(description = "Id специальности", example = "3")
  private Long id;

  @Schema(description = "Название специальности", example = "Backend developer")
  private String name;
}
