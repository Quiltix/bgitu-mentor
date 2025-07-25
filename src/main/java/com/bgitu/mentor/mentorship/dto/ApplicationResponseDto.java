package com.bgitu.mentor.mentorship.dto;


import com.bgitu.mentor.mentorship.model.Application;
import com.bgitu.mentor.mentorship.model.ApplicationStatus;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationResponseDto {

    Long id;

    @Size(max = 250, message = "Сократите сообщение до 250 символов")
    String message;

    ApplicationStatus status;

    StudentPreviewDto student;

    public ApplicationResponseDto(Application application) {
        this.id = application.getId();
        this.message = application.getMessage();
        this.status = application.getStatus();
        this.student =  new StudentPreviewDto(application.getStudent()) ;
    }
}