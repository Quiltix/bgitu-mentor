package com.bgitu.mentor.vote.service;


import com.bgitu.mentor.user.model.BaseUser;
import com.bgitu.mentor.user.service.UserService;
import com.bgitu.mentor.vote.data.model.Votable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VotingService {

    private final UserService userService;

    @Transactional
    public <T extends Votable> void vote(Long entityId, Long userId, boolean isUpvote, VoteHandler<T> handler){
        BaseUser user = userService.findById(userId);

        if (handler.hasVoted(user, entityId)){
            throw new IllegalStateException("Вы уже голосовали");
        }

        T entity = handler.findVotableEntity(entityId);

        handler.saveVote(user, entity, isUpvote);

        int rankChange = isUpvote ? 1 : -1;
        entity.setRank(entity.getRank() + rankChange);

        handler.saveVotableEntity(entity);

    }

}
