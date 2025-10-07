package com.bgitu.mentor.vote.service;

import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import com.bgitu.mentor.student.data.model.Student;
import com.bgitu.mentor.user.data.model.BaseUser;
import com.bgitu.mentor.vote.data.model.MentorVote;
import com.bgitu.mentor.vote.data.repository.MentorVoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MentorVoteHandlerTest {

  @Mock MentorRepository mentorRepository;
  @Mock MentorVoteRepository voteRepository;

  @InjectMocks MentorVoteHandler mentorVoteHandler;

  @DisplayName("hasVoted | Should return true when vote exists for given user and mentor")
  @Test
  void hasVoted_returnsTrue_whenVoteExists() {
    Long userId = 1L;
    Long mentorId = 2L;

    when(voteRepository.existsByMentorIdAndUserId(mentorId, userId)).thenReturn(true);
    when(mentorRepository.existsById(mentorId)).thenReturn(true);

    boolean result = mentorVoteHandler.hasVoted(userId, mentorId);

    verify(voteRepository, times(1)).existsByMentorIdAndUserId(mentorId, userId);
    verify(mentorRepository, times(1)).existsById(mentorId);
    assertTrue(result);
  }

  @DisplayName("hasVoted | Should return false when mentor does not exist")
  @Test
  void hasVoted_returnsFalse_whenMentorNotFound() {
    Long userId = 1L;
    Long mentorId = 2L;

    when(mentorRepository.existsById(mentorId)).thenReturn(false);

    boolean result = mentorVoteHandler.hasVoted(userId, mentorId);

    verify(voteRepository, never()).existsByMentorIdAndUserId(mentorId, userId);
    verify(mentorRepository, times(1)).existsById(mentorId);
    assertFalse(result);
  }

  @DisplayName("hasVoted | Should return false when vote does not exist for given user and mentor")
  @Test
  void hasVoted_returnsFalse_whenVoteDoesNotExist() {
    Long userId = 1L;
    Long mentorId = 2L;

    when(voteRepository.existsByMentorIdAndUserId(mentorId, userId)).thenReturn(false);
    when(mentorRepository.existsById(mentorId)).thenReturn(true);

    boolean result = mentorVoteHandler.hasVoted(userId, mentorId);

    verify(voteRepository, times(1)).existsByMentorIdAndUserId(mentorId, userId);
    assertFalse(result);
  }

  @DisplayName("findVotableEntity | Should return mentor when mentor exists")
  @Test
  void findVotableEntity_returnsMentor_whenMentorExists() {
    Long mentorId = 1L;
    Mentor mentor = new Mentor();
    mentor.setId(mentorId);

    when(mentorRepository.findById(mentorId)).thenReturn(Optional.of(mentor));

    Mentor result = mentorVoteHandler.findVotableEntity(mentorId);

    verify(mentorRepository, times(1)).findById(mentorId);
    assertNotNull(result);
    assertEquals(mentorId, result.getId());
  }

  @DisplayName("findVotableEntity | Should throw exception when mentor does not exist")
  @Test
  void findVotableEntity_throwsException_whenMentorDoesNotExist() {
    Long mentorId = 1L;

    when(mentorRepository.findById(mentorId)).thenReturn(Optional.empty());

    assertThrows(
        EntityNotFoundException.class, () -> mentorVoteHandler.findVotableEntity(mentorId));

    verify(mentorRepository, times(1)).findById(mentorId);
  }

  @DisplayName("saveVote | Should save upvote for mentor and associate it with user")
  @Test
  void saveVote_savesUpvoteForMentorAndAssociatesWithUser() {
    BaseUser user = new Student();
    user.setId(1L);

    Mentor mentor = new Mentor();
    mentor.setId(2L);

    MentorVote vote = new MentorVote();
    vote.setUser(user);
    vote.setMentor(mentor);
    vote.setUpvote(true);

    when(voteRepository.save(any(MentorVote.class))).thenReturn(vote);

    mentorVoteHandler.saveVote(user, mentor, true);

    verify(voteRepository, times(1))
        .save(
            argThat(
                savedVote ->
                    savedVote.getUser().equals(user)
                        && savedVote.getMentor().equals(mentor)
                        && savedVote.isUpvote()));
  }

  @DisplayName("saveVote | Should save downvote for mentor and associate it with user")
  @Test
  void saveVote_savesDownVoteForMentorAndAssociatesWithUser() {
    BaseUser user = new Student();
    user.setId(1L);

    Mentor mentor = new Mentor();
    mentor.setId(2L);

    MentorVote vote = new MentorVote();
    vote.setUser(user);
    vote.setMentor(mentor);
    vote.setUpvote(false);

    when(voteRepository.save(any(MentorVote.class))).thenReturn(vote);

    mentorVoteHandler.saveVote(user, mentor, false);

    verify(voteRepository, times(1))
        .save(
            argThat(
                savedVote ->
                    savedVote.getUser().equals(user)
                        && savedVote.getMentor().equals(mentor)
                        && !savedVote.isUpvote()));
  }
}
