package com.bgitu.mentor.mentorship.dto;


import com.bgitu.mentor.mentorship.model.ApplicationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationResponseDto {

    Long id;

    String message;

    ApplicationStatus status;

    StudentPreviewDto student;

    public ApplicationResponseDto(Long id, String message, ApplicationStatus status, StudentPreviewDto student) {
        this.id = id;
        this.message = message;
        this.status = status;
        this.student = student;
    }
}