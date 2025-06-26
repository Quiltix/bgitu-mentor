package com.bgitu.mentor.article.repository;

import com.bgitu.mentor.article.model.Article;
import com.bgitu.mentor.article.model.ArticleVote;
import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.student.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleVoteRepository extends JpaRepository<ArticleVote, Long> {

    boolean existsByArticleIdAndMentorId(Long articleId, Long mentorId);

    boolean existsByArticleIdAndStudentId(Long articleId, Long studentId);

    Optional<ArticleVote> findByArticleAndStudent(Article article, Student student);

    Optional<ArticleVote> findByArticleAndMentor(Article article, Mentor mentor);
}
