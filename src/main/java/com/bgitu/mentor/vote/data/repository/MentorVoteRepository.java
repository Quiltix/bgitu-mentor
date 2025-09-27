package com.bgitu.mentor.vote.data.repository;

import com.bgitu.mentor.vote.data.model.MentorVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MentorVoteRepository extends JpaRepository<MentorVote, Long> {

  boolean existsByMentorIdAndUserId(Long entityId, Long id);

  Optional<MentorVote> findByUserIdAndMentorId(Long userId, Long mentorId);
}
