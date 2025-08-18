package com.bgitu.mentor.vote.service;

import com.bgitu.mentor.student.data.model.Student;
import com.bgitu.mentor.user.data.model.BaseUser;
import com.bgitu.mentor.user.service.UserFinder;
import com.bgitu.mentor.vote.data.model.Votable;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotingServiceTest {

  @Mock private UserFinder userFinder;
  @Mock private VoteHandler<FakeVotableEntity> voteHandler;

  @InjectMocks private VotingService votingService;

  private BaseUser votingUser;
  private FakeVotableEntity votableEntity;

  @BeforeEach
  void setUp() {
    votingUser = new Student();
    votingUser.setId(10L);

    votableEntity = new FakeVotableEntity();
    votableEntity.setId(100L);
    votableEntity.setRank(10);
  }

  @Test
  @DisplayName(
      "vote | Должен успешно проголосовать 'за' (upvote), если пользователь еще не голосовал")
  void vote_shouldSuccessfullyUpvote_whenUserHasNotVoted() {
    long entityId = votableEntity.getId();
    long userId = votingUser.getId();
    boolean isUpvote = true;

    when(userFinder.findUserById(userId)).thenReturn(votingUser);
    when(voteHandler.hasVoted(userId, entityId)).thenReturn(false);
    when(voteHandler.findVotableEntity(entityId)).thenReturn(votableEntity);

    votingService.vote(entityId, userId, isUpvote, voteHandler);

    assertEquals(11, votableEntity.getRank());

    verify(userFinder, times(1)).findUserById(userId);
    verify(voteHandler, times(1)).hasVoted(userId, entityId);
    verify(voteHandler, times(1)).findVotableEntity(entityId);

    verify(voteHandler, times(1)).saveVote(votingUser, votableEntity, isUpvote);

    verify(voteHandler, times(1)).saveVotableEntity(votableEntity);
  }

  @Test
  @DisplayName(
      "vote | Должен успешно проголосовать 'против' (downvote), если пользователь еще не голосовал")
  void vote_shouldSuccessfullyDownvote_whenUserHasNotVoted() {

    long entityId = votableEntity.getId();
    long userId = votingUser.getId();
    boolean isUpvote = false;

    when(userFinder.findUserById(userId)).thenReturn(votingUser);
    when(voteHandler.hasVoted(userId, entityId)).thenReturn(false);
    when(voteHandler.findVotableEntity(entityId)).thenReturn(votableEntity);

    votingService.vote(entityId, userId, isUpvote, voteHandler);

    assertEquals(9, votableEntity.getRank());

    // Проверяем вызовы
    verify(voteHandler, times(1)).saveVote(votingUser, votableEntity, isUpvote);
    verify(voteHandler, times(1)).saveVotableEntity(votableEntity);
  }

  @Test
  @DisplayName("vote | Должен выбросить IllegalStateException, если пользователь уже голосовал")
  void vote_shouldThrowIllegalStateException_whenUserHasAlreadyVoted() {

    long entityId = votableEntity.getId();
    long userId = votingUser.getId();

    when(userFinder.findUserById(userId)).thenReturn(votingUser);

    when(voteHandler.hasVoted(userId, entityId)).thenReturn(true);

    IllegalStateException exception =
        assertThrows(
            IllegalStateException.class,
            () -> {
              votingService.vote(entityId, userId, true, voteHandler);
            });

    assertEquals("Вы уже голосовали", exception.getMessage());

    // Критически важно: убедимся, что никакие изменяющие методы не были вызваны
    verify(voteHandler, never()).findVotableEntity(anyLong());
    verify(voteHandler, never()).saveVote(any(), any(), anyBoolean());
    verify(voteHandler, never()).saveVotableEntity(any());
  }

  private static class FakeVotableEntity implements Votable {
    @Setter private Long id;
    private Integer rank;

    @Override
    public Long getId() {
      return id;
    }

    @Override
    public Integer getRank() {
      return rank;
    }

    @Override
    public void setRank(Integer rank) {
      this.rank = rank;
    }
  }
}
