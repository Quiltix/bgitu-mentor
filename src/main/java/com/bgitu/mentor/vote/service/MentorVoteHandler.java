package com.bgitu.mentor.vote.service;


import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import com.bgitu.mentor.user.data.model.BaseUser;
import com.bgitu.mentor.vote.data.model.MentorVote;
import com.bgitu.mentor.vote.data.repository.MentorVoteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MentorVoteHandler implements VoteHandler<Mentor>{

    private final MentorRepository mentorRepository;
    private final MentorVoteRepository voteRepository;


    @Override
    public boolean hasVoted(BaseUser user, Long entityId) {
        return voteRepository.existsByMentorIdAndUserId(entityId, user.getId());
    }

    @Override
    public Mentor findVotableEntity(Long entityId) {
        return mentorRepository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException("Ментор не найден"));
    }

    @Override
    public void saveVote(BaseUser user, Mentor entity, boolean upVote) {
        MentorVote vote = new MentorVote();
        vote.setUser(user);
        vote.setMentor(entity);
        vote.setUpvote(upVote);
        voteRepository.save(vote);

    }

    @Override
    public void saveVotableEntity(Mentor entity) {
        mentorRepository.save(entity);

    }


}