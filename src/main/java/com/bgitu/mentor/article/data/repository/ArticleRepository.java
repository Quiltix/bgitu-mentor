package com.bgitu.mentor.article.data.repository;

import com.bgitu.mentor.article.data.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ArticleRepository
    extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {

  List<Article> findAllByAuthorId(Long id);

  List<Article> findTop3ByOrderByRankDesc();

  boolean existsByTitle(String title);
}
