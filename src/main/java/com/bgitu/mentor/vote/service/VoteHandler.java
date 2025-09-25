package com.bgitu.mentor.vote.service;

import com.bgitu.mentor.user.data.model.BaseUser;
import com.bgitu.mentor.vote.data.model.Votable;

public interface VoteHandler<T extends Votable> {

  boolean hasVoted(Long userId, Long entityId);

  T findVotableEntity(Long entityId);

  void saveVote(BaseUser user, T entity, boolean upVote);

  void saveVotableEntity(T entity);

  int getResultVote(Long UserId, Long entityId);
}
