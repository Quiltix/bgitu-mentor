package com.bgitu.mentor.mentorship.data.dto;


import com.bgitu.mentor.mentorship.data.model.Application;
import com.bgitu.mentor.mentorship.data.model.ApplicationStatus;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationDetailsResponseDto {

    Long id;

    @Size(max = 250, message = "Сократите сообщение до 250 символов")
    String message;

    ApplicationStatus status;

    StudentPreviewDto student;

    public ApplicationDetailsResponseDto(Application application) {
        this.id = application.getId();
        this.message = application.getMessage();
        this.status = application.getStatus();
        this.student =  new StudentPreviewDto(application.getStudent()) ;
    }
}