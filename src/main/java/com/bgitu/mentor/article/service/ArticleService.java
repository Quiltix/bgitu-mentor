package com.bgitu.mentor.article.service;

import com.bgitu.mentor.article.data.dto.ArticleCreateDto;
import com.bgitu.mentor.article.data.dto.ArticleResponseDto;
import com.bgitu.mentor.article.data.dto.ArticleShortDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


public interface ArticleService {


    ArticleResponseDto createArticle(Long authorId, ArticleCreateDto dto, MultipartFile image);
    ArticleResponseDto getById(Long id);
    Page<ArticleShortDto> findArticles(Long specialityId, String query, Pageable pageable);
    void deleteArticle(Long articleId, Long userId);
    void changeArticleRank(Long articleId, boolean like, Long userId);
}
