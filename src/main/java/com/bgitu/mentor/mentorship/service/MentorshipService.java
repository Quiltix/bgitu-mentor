package com.bgitu.mentor.mentorship.service;

import com.bgitu.mentor.mentorship.data.dto.ApplicationDecisionRequestDto;
import com.bgitu.mentor.mentorship.data.dto.ApplicationDetailsResponseDto;
import com.bgitu.mentor.mentorship.data.dto.ApplicationCreateRequestDto;
import com.bgitu.mentor.mentorship.data.model.ApplicationStatus;
import com.bgitu.mentor.student.data.dto.ApplicationOfStudentResponseDto;

import java.util.List;

public interface MentorshipService {

    ApplicationDetailsResponseDto createApplication(Long studentId, ApplicationCreateRequestDto dto);

    void updateApplicationStatus(Long mentorId, Long applicationId, ApplicationDecisionRequestDto dto);

    List<ApplicationDetailsResponseDto> getApplicationsForMentor(Long mentorId, ApplicationStatus status);
    List<ApplicationOfStudentResponseDto> getApplicationsForStudent(Long studentId, ApplicationStatus status);
}
