package com.bgitu.mentor.student.data.dto;


import com.bgitu.mentor.mentorship.data.model.ApplicationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationOfStudentResponseDto {
    private Long id;
    private String message;
    private String mentorFullName;
    private String mentorSpeciality;
    private ApplicationStatus status;

}
