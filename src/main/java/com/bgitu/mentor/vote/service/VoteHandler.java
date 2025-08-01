package com.bgitu.mentor.vote.service;

import com.bgitu.mentor.user.model.BaseUser;
import com.bgitu.mentor.vote.data.model.Votable;

public interface VoteHandler<T extends Votable>{

    boolean hasVoted(BaseUser user, Long entityId);

    T findVotableEntity(Long entityId);

    void saveVote(BaseUser user, T entity, boolean upVote);

    void saveVotableEntity(T entity);
}
