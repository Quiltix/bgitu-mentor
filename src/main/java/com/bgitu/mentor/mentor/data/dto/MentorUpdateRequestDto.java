package com.bgitu.mentor.mentor.data.dto;


import com.bgitu.mentor.user.dto.BaseUserUpdateRequestDto;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MentorUpdateRequestDto extends BaseUserUpdateRequestDto {

    private Long specialityId;
}
