package com.bgitu.mentor.mentorship.service;

import com.bgitu.mentor.mentorship.dto.UpdateApplicationStatusDto;
import com.bgitu.mentor.mentorship.dto.ApplicationResponseDto;
import com.bgitu.mentor.mentorship.dto.MentorshipRequestDto;
import com.bgitu.mentor.mentorship.model.ApplicationStatus;

import java.util.List;

public interface MentorshipService {

    ApplicationResponseDto createApplication(Long studentId, MentorshipRequestDto dto);

    void updateApplicationStatus(Long mentorId, Long applicationId, UpdateApplicationStatusDto dto);

    List<ApplicationResponseDto> getApplicationsForMentor(Long mentorId, ApplicationStatus status);
}
