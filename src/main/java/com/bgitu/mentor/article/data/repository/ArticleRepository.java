package com.bgitu.mentor.article.data.repository;

import com.bgitu.mentor.article.data.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article> {


    List<Article> findAllByOrderByRankDesc();

    List<Article> findBySpecialityIdOrderByRankDesc(Long specialityId);

    List<Article> findTop3ByOrderByRankDesc();

    @Query("SELECT a FROM Article a WHERE " +
            "LOWER(a.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(a.content) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Article> searchByTitleOrContent(@Param("query") String query);
}