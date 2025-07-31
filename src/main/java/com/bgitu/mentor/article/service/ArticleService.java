package com.bgitu.mentor.article.service;

import com.bgitu.mentor.article.data.dto.ArticleCreateRequestDto;
import com.bgitu.mentor.article.data.dto.ArticleDetailsResponseDto;
import com.bgitu.mentor.article.data.dto.ArticleSummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


public interface ArticleService {


    ArticleDetailsResponseDto createArticle(Long authorId, ArticleCreateRequestDto dto, MultipartFile image);
    ArticleDetailsResponseDto getById(Long id);
    Page<ArticleSummaryResponseDto> findArticles(Long specialityId, String query, Pageable pageable);
    void deleteArticle(Long articleId, Long userId);
    void changeArticleRank(Long articleId, boolean like, Long userId);
}
