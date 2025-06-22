package com.bgitu.mentor.mentorship.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationDecisionDto {

    @NotNull
    Long applicationId;

    @NotNull
    Boolean accepted;
}
