package com.bgitu.mentor.student.dto;

import com.bgitu.mentor.mentorship.model.Application;
import com.bgitu.mentor.mentorship.model.ApplicationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationStudentDto {
    private Long id;
    private String message;
    private String mentorFullName;
    private String mentorSpeciality;
    private ApplicationStatus status;

    public ApplicationStudentDto(Application application) {
        this.id = application.getId();
        this.message = application.getMessage();
        this.status = application.getStatus();
        if (application.getMentor() != null) {
            this.mentorFullName = application.getMentor().getFirstName() + " " + application.getMentor().getLastName();
            this.mentorSpeciality = application.getMentor().getSpeciality() != null
                    ? application.getMentor().getSpeciality().getName()
                    : null;
        }
    }
}
