package com.bgitu.mentor.article.repository;

import com.bgitu.mentor.article.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {


    List<Article> findAllByOrderByRankDesc();

    List<Article> findBySpecialityIdOrderByRankDesc(Long specialityId);

    List<Article> findTop3ByOrderByRankDesc();

    @Query("SELECT a FROM Article a WHERE " +
            "LOWER(a.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(a.content) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Article> searchByTitleOrContent(@Param("query") String query);
}