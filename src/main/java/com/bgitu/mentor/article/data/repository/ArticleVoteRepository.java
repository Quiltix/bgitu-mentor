package com.bgitu.mentor.article.data.repository;

import com.bgitu.mentor.article.data.model.Article;
import com.bgitu.mentor.article.data.model.ArticleVote;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleVoteRepository extends JpaRepository<ArticleVote, Long> {

    boolean existsByArticleIdAndMentorId(Long articleId, Long mentorId);

    boolean existsByArticleIdAndStudentId(Long articleId, Long studentId);

    void deleteAllByArticleId(Long articleId);

}
