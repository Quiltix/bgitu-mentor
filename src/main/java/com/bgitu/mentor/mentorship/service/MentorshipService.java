package com.bgitu.mentor.mentorship.service;

import com.bgitu.mentor.auth.Role;
import com.bgitu.mentor.mentorship.data.dto.ApplicationDecisionRequestDto;
import com.bgitu.mentor.mentorship.data.dto.ApplicationDetailsResponseDto;
import com.bgitu.mentor.mentorship.data.dto.ApplicationCreateRequestDto;
import com.bgitu.mentor.mentorship.data.model.ApplicationStatus;

import java.util.List;

public interface MentorshipService {

  ApplicationDetailsResponseDto createApplication(Long studentId, ApplicationCreateRequestDto dto);

  void updateApplicationStatus(
      Long mentorId, Long applicationId, ApplicationDecisionRequestDto dto);

  List<?> getMyApplications(Long userId, Role role, ApplicationStatus status);
}
