package com.bgitu.mentor.article.service;

import com.bgitu.mentor.article.data.dto.ArticleCreateRequestDto;
import com.bgitu.mentor.article.data.dto.ArticleDetailsResponseDto;
import com.bgitu.mentor.article.data.dto.ArticleSummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {

  ArticleDetailsResponseDto createArticle(
      Long authorId, ArticleCreateRequestDto dto, MultipartFile image);

  ArticleDetailsResponseDto getById(Long id, Long userId);

  Page<ArticleSummaryResponseDto> findArticles(Long specialityId, String query, Pageable pageable);

  List<ArticleSummaryResponseDto> findArticlesByAuthor(Long authorId);

  List<ArticleSummaryResponseDto> findPopularArticles();

  void deleteArticle(Long articleId, Long userId);

  void changeArticleRank(Long articleId, boolean like, Long userId);
}
