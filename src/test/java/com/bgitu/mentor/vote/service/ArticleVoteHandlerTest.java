package com.bgitu.mentor.vote.service;

import com.bgitu.mentor.article.data.model.Article;
import com.bgitu.mentor.article.data.repository.ArticleRepository;
import com.bgitu.mentor.student.data.model.Student;
import com.bgitu.mentor.user.data.model.BaseUser;
import com.bgitu.mentor.vote.data.repository.ArticleVoteRepository;
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
class ArticleVoteHandlerTest {

  @Mock private ArticleRepository articleRepository;
  @Mock private ArticleVoteRepository voteRepository;

  @InjectMocks private ArticleVoteHandler articleVoteHandler;

  @DisplayName("findVotableEntity | Should return article when article exists")
  @Test
  void findVotableEntity_returnsArticle_whenArticleExists() {
    Long articleId = 1L;
    Article article = new Article();
    article.setId(articleId);

    when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));

    Article result = articleVoteHandler.findVotableEntity(articleId);

    verify(articleRepository, times(1)).findById(articleId);
    assertNotNull(result);
    assertEquals(articleId, result.getId());
  }

  @DisplayName("findVotableEntity | Should throw exception when article does not exist")
  @Test
  void findVotableEntity_throwsException_whenArticleDoesNotExist() {
    Long articleId = 1L;

    when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

    assertThrows(
        EntityNotFoundException.class, () -> articleVoteHandler.findVotableEntity(articleId));

    verify(articleRepository, times(1)).findById(articleId);
  }

  @DisplayName("saveVote | Should add upvote to article and associate it with user")
  @Test
  void saveVote_addsUpvoteToArticleAndAssociatesWithUser() {
    BaseUser user = new Student();
    user.setId(1L);

    Article article = mock(Article.class);

    articleVoteHandler.saveVote(user, article, true);

    verify(article, times(1))
        .addVote(argThat(vote -> vote.getUser().equals(user) && vote.isUpvote()));
  }

  @DisplayName("saveVote | Should add down vote to article and associate it with user")
  @Test
  void saveVote_addsDownvoteToArticleAndAssociatesWithUser() {
    BaseUser user = new Student();
    user.setId(1L);

    Article article = mock(Article.class);

    articleVoteHandler.saveVote(user, article, false);

    verify(article, times(1))
        .addVote(argThat(vote -> vote.getUser().equals(user) && !vote.isUpvote()));
  }
}
