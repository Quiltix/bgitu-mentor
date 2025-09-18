package com.bgitu.mentor.mentor.data.dto;

import com.bgitu.mentor.user.data.dto.BaseUserUpdateRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentorUpdateRequestDto extends BaseUserUpdateRequestDto {

  @Schema(description = "Id специальности ", example = "3")
  private Long specialityId;
}
