package com.bgitu.mentor.vote.service;

import com.bgitu.mentor.article.data.model.Article;
import com.bgitu.mentor.article.data.repository.ArticleRepository;
import com.bgitu.mentor.vote.data.repository.ArticleVoteRepository;
import com.bgitu.mentor.user.model.BaseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ArticleVoteHandler implements VoteHandler<Article>{

    private final ArticleRepository articleRepository;
    private final ArticleVoteRepository voteRepository;


    @Override
    public boolean hasVoted(BaseUser user, Long entityId) {
        return voteRepository.ex;
    }

    @Override
    public Article findVotableEntity(Long EntityId) {
        return null;
    }

    @Override
    public void saveVote(BaseUser user, Article Entity, boolean upVote) {

    }

    @Override
    public void saveVotableEntity(Article entity) {

    }
}
