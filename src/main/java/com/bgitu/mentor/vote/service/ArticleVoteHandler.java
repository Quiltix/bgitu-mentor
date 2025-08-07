package com.bgitu.mentor.vote.service;

import com.bgitu.mentor.article.data.model.Article;
import com.bgitu.mentor.article.data.repository.ArticleRepository;
import com.bgitu.mentor.vote.data.model.ArticleVote;
import com.bgitu.mentor.vote.data.repository.ArticleVoteRepository;
import com.bgitu.mentor.user.data.model.BaseUser;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ArticleVoteHandler implements VoteHandler<Article>{

    private final ArticleRepository articleRepository;
    private final ArticleVoteRepository voteRepository;


    @Override
    public boolean hasVoted(Long userId, Long entityId) {
        return voteRepository.existsByArticleIdAndUserId(entityId, userId);
    }

    @Override
    public Article findVotableEntity(Long entityId) {
        return articleRepository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException("Статья не найдена"));
    }

    @Override
    public void saveVote(BaseUser user, Article entity, boolean isUpvote) {
        ArticleVote vote = new ArticleVote();
        vote.setUser(user);
        vote.setUpvote(isUpvote);
        entity.addVote(vote);

    }

    @Override
    public void saveVotableEntity(Article entity) {
        articleRepository.save(entity);

    }
}
