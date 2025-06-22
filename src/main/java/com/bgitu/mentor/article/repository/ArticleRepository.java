package com.bgitu.mentor.article.repository;

import com.bgitu.mentor.article.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {


    List<Article> findAllByOrderByRankDesc();

    List<Article> findBySpecialityIdOrderByRankDesc(Long specialityId);

    List<Article> findTop3ByOrderByRankDesc();
}