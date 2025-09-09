package com.bgitu.mentor.mentor.service;

import com.bgitu.mentor.vote.data.dto.ChangedRankResponseDto;
import com.bgitu.mentor.mentor.data.MentorMapper;
import com.bgitu.mentor.mentor.data.MentorSpecifications;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.mentor.data.dto.MentorSummaryResponseDto;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import com.bgitu.mentor.vote.service.MentorVoteHandler;
import com.bgitu.mentor.vote.service.VotingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorDirectoryServiceImpl implements MentorDirectoryService {

  private final MentorRepository mentorRepository;
  private final VotingService votingService;
  private final MentorVoteHandler mentorVoteHandler;
  private final MentorMapper mentorMapper;

  @Override
  @Transactional(readOnly = true)
  public Page<MentorSummaryResponseDto> findMentors(
      Long specialityId, String query, Pageable pageable) {

    Specification<Mentor> specification = Specification.not(null);

    if (specialityId != null) {
      specification = specification.and(MentorSpecifications.hasSpeciality(specialityId));
    }

    if (query != null && !query.isBlank()) {
      if (query.length() > 250) {
        throw new IllegalStateException("Строка для поиска слишком длинная");
      }
      specification = specification.and(MentorSpecifications.nameOrDescriptionContains(query));
    }

    Page<Mentor> mentorPage = mentorRepository.findAll(specification, pageable);

    return mentorPage.map(mentorMapper::toSummaryDto);
  }

  @Override
  @Transactional(readOnly = true)
  public MentorDetailsResponseDto getMentorDetails(Long mentorId, Long userId) {
    Mentor mentor =
        mentorRepository
            .findById(mentorId)
            .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

    return mentorMapper.toDetailsDto(mentor, !mentorVoteHandler.hasVoted(userId, mentorId));
  }

  @Override
  @Transactional
  public ChangedRankResponseDto voteForMentor(Long mentorId, boolean upvote, Long userId) {
    return (new ChangedRankResponseDto(
        votingService.vote(mentorId, userId, upvote, mentorVoteHandler)));
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "popularMentors")
  public List<MentorSummaryResponseDto> findPopularMentors() {

    List<Mentor> popularMentors = mentorRepository.findTop3ByOrderByRankDesc();

    return mentorMapper.toSummaryDtoList(popularMentors);
  }
}
