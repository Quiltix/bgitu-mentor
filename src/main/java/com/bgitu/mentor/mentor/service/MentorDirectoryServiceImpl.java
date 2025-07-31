package com.bgitu.mentor.mentor.service;


import com.bgitu.mentor.mentor.data.MentorSpecifications;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.mentor.data.dto.MentorSummaryResponseDto;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import com.bgitu.mentor.vote.service.MentorVoteHandler;
import com.bgitu.mentor.vote.service.VotingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentorDirectoryServiceImpl implements MentorDirectoryService{

    private final MentorRepository mentorRepository;
    private final VotingService votingService;
    private final MentorVoteHandler mentorVoteHandler;

    @Override
    public Page<MentorSummaryResponseDto> findMentors(Long specialityId, String query, Pageable pageable) {

        Specification<Mentor> specification = Specification.not(null);

        if (specialityId != null) {
            specification.and(MentorSpecifications.hasSpeciality(specialityId));
        }

        if (query != null && !query.isBlank()) {
            if (query.length() > 250) {
                throw new IllegalStateException("Строка для поиска слишком длинная");
            }
            specification.and(MentorSpecifications.nameOrDescriptionContains(query));
        }

        Page<Mentor> mentorPage = mentorRepository.findAll(specification, pageable);

        return mentorPage.map(MentorSummaryResponseDto::new);
    }

    @Override
    public MentorDetailsResponseDto getMentorDetails(Long mentorId) {
        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        return new MentorDetailsResponseDto(mentor);
    }

    @Override
    @Transactional
    public void voteForMentor(Long mentorId, boolean upvote, Long userId) {
        votingService.vote(mentorId, userId, upvote, mentorVoteHandler);
    }
}
