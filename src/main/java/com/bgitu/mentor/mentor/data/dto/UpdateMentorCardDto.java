package com.bgitu.mentor.mentor.data.dto;


import com.bgitu.mentor.user.dto.UpdateBaseUserCardDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateMentorCardDto extends UpdateBaseUserCardDto {

    private Long specialityId;
}
