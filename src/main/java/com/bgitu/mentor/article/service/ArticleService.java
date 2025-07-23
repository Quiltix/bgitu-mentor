package com.bgitu.mentor.article.service;

import com.bgitu.mentor.article.dto.ArticleCreateDto;
import com.bgitu.mentor.article.dto.ArticleResponseDto;
import com.bgitu.mentor.article.dto.ArticleShortDto;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ArticleService {


    ArticleResponseDto createArticle(Authentication auth, ArticleCreateDto dto, MultipartFile image);
    ArticleResponseDto getById(Long id);
    List<ArticleShortDto> getAllArticles(Optional<Long> specialityId);
    void deleteArticle(Long articleId, Authentication authentication);
    void changeArticleRank(Long articleId, boolean like, Authentication auth);
    List<ArticleShortDto> getTop3Articles();
    List<ArticleShortDto> searchArticles(String query);
    ArticleResponseDto getById(Long id, Authentication auth);
}
