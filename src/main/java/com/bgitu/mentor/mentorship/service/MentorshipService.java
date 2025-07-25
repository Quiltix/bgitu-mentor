package com.bgitu.mentor.mentorship.service;

import com.bgitu.mentor.mentorship.dto.UpdateApplicationStatusDto;
import com.bgitu.mentor.mentorship.dto.ApplicationResponseDto;
import com.bgitu.mentor.mentorship.dto.MentorshipRequestDto;
import com.bgitu.mentor.mentorship.model.ApplicationStatus;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MentorshipService {

    ApplicationResponseDto createApplication(Authentication authentication, MentorshipRequestDto dto);

    void updateApplicationStatus(UpdateApplicationStatusDto dto);

    List<ApplicationResponseDto> getApplicationsForMentor(Authentication authentication, ApplicationStatus status);
}
