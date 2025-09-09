package com.bgitu.mentor.mentor.service;

import com.bgitu.mentor.vote.data.dto.ChangedRankResponseDto;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.mentor.data.dto.MentorSummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MentorDirectoryService {

  Page<MentorSummaryResponseDto> findMentors(Long specialityId, String query, Pageable pageable);

  MentorDetailsResponseDto getMentorDetails(Long mentorId, Long userId);

  ChangedRankResponseDto voteForMentor(Long mentorId, boolean isUpvote, Long votingUserId);

  List<MentorSummaryResponseDto> findPopularMentors();
}
