package com.bgitu.mentor.mentor.service;

import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.mentor.data.dto.MentorSummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MentorDirectoryService {

  Page<MentorSummaryResponseDto> findMentors(Long specialityId, String query, Pageable pageable);

  MentorDetailsResponseDto getMentorDetails(Long mentorId);

  void voteForMentor(Long mentorId, boolean isUpvote, Long votingUserId);
}
