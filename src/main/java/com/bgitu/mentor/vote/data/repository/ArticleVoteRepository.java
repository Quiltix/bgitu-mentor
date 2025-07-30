package com.bgitu.mentor.vote.data.repository;

import com.bgitu.mentor.vote.data.model.ArticleVote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleVoteRepository extends JpaRepository<ArticleVote, Long> {

    boolean existsByArticleIdAndMentorId(Long articleId, Long mentorId);

    boolean existsByArticleIdAndStudentId(Long articleId, Long studentId);


    boolean existsByArticleIdAndUserId(Long articleId, Long userId);

    void deleteAllByArticleId(Long articleId);

}
