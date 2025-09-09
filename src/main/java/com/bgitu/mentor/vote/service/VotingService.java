package com.bgitu.mentor.vote.service;

import com.bgitu.mentor.user.data.model.BaseUser;
import com.bgitu.mentor.user.service.UserFinder;
import com.bgitu.mentor.vote.data.model.Votable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VotingService {

  private final UserFinder userFinder;

  @Transactional
  public <T extends Votable> Integer vote(
      Long entityId, Long userId, boolean isUpvote, VoteHandler<T> handler) {
    BaseUser user = userFinder.findUserById(userId);

    if (handler.hasVoted(userId, entityId)) {
      throw new IllegalStateException("Вы уже голосовали");
    }

    T entity = handler.findVotableEntity(entityId);

    handler.saveVote(user, entity, isUpvote);

    int rankChange = isUpvote ? 1 : -1;
    entity.setRank(entity.getRank() + rankChange);

    handler.saveVotableEntity(entity);
    return rankChange;
  }
}
