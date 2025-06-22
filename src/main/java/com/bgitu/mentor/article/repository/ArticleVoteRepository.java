package com.bgitu.mentor.article.repository;

import com.bgitu.mentor.article.model.ArticleVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleVoteRepository extends JpaRepository<ArticleVote, Long> {

    boolean existsByArticleIdAndMentorId(Long articleId, Long mentorId);

    boolean existsByArticleIdAndStudentId(Long articleId, Long studentId);
}
