package com.bgitu.mentor.mentorship.service;

import com.bgitu.mentor.mentorship.dto.ApplicationDecisionRequestDto;
import com.bgitu.mentor.mentorship.dto.ApplicationDetailsResponseDto;
import com.bgitu.mentor.mentorship.dto.ApplicationCreateRequestDto;
import com.bgitu.mentor.mentorship.model.ApplicationStatus;

import java.util.List;

public interface MentorshipService {

    ApplicationDetailsResponseDto createApplication(Long studentId, ApplicationCreateRequestDto dto);

    void updateApplicationStatus(Long mentorId, Long applicationId, ApplicationDecisionRequestDto dto);

    List<ApplicationDetailsResponseDto> getApplicationsForMentor(Long mentorId, ApplicationStatus status);
}
