package com.bgitu.mentor.mentorship.service;

import com.bgitu.mentor.mentorship.dto.ApplicationDecisionDto;
import com.bgitu.mentor.mentorship.dto.ApplicationResponseDto;
import com.bgitu.mentor.mentorship.dto.MentorshipRequestDto;
import com.bgitu.mentor.mentorship.model.ApplicationStatus;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MentorshipService {

    void requestMentorship(Authentication authentication, MentorshipRequestDto dto);
    void respondToApplication(ApplicationDecisionDto dto);
    List<ApplicationResponseDto> getApplicationsForMentor(Authentication authentication, ApplicationStatus status);
    void studentRejectMentorship(Authentication authentication);
    void mentorRejectStudent(Authentication authentication, Long studentId);
}
