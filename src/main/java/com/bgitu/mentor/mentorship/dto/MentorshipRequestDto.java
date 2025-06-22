package com.bgitu.mentor.mentorship.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MentorshipRequestDto {

    @NotNull
    Long mentorId;
    @NotNull
    String message;
}
