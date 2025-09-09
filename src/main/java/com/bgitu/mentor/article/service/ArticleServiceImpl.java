package com.bgitu.mentor.article.service;

import com.bgitu.mentor.article.data.ArticleMapper;
import com.bgitu.mentor.article.data.ArticleSpecifications;
import com.bgitu.mentor.article.data.dto.ArticleCreateRequestDto;
import com.bgitu.mentor.article.data.dto.ArticleDetailsResponseDto;
import com.bgitu.mentor.article.data.dto.ArticleSummaryResponseDto;
import com.bgitu.mentor.article.data.model.Article;
import com.bgitu.mentor.article.data.repository.ArticleRepository;
import com.bgitu.mentor.common.dto.ChangedRankResponseDto;
import com.bgitu.mentor.speciality.service.SpecialityService;
import com.bgitu.mentor.user.service.UserFinder;
import com.bgitu.mentor.common.service.FileStorageService;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.speciality.data.model.Speciality;
import com.bgitu.mentor.vote.service.ArticleVoteHandler;
import com.bgitu.mentor.vote.service.VotingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleServiceImpl implements ArticleService {
  private final ArticleRepository articleRepository;
  private final UserFinder userFinder;
  private final SpecialityService specialityService;
  private final FileStorageService fileStorageService;
  private final VotingService votingService;
  private final ArticleVoteHandler articleVoteHandler;
  private final ArticleMapper articleMapper;

  @Override
  @Transactional
  public ArticleDetailsResponseDto createArticle(
      Long authorId, ArticleCreateRequestDto dto, MultipartFile image) {
    Mentor author = userFinder.findMentorById(authorId);
    Speciality speciality = specialityService.getById(dto.getSpecialityId());

    Article article = new Article();
    article.setTitle(dto.getTitle());
    article.setContent(dto.getContent());
    article.setAuthor(author);
    article.setSpeciality(speciality);
    article.setRank(0);

    if (image != null && !image.isEmpty()) {
      String storedRelativePath = fileStorageService.store(image, "articles");
      String publicUrl = "/api/uploads/image/" + storedRelativePath.replace("\\", "/");
      article.setImageUrl(publicUrl);
    }
    return articleMapper.toDetailsDto(articleRepository.save(article));
  }

  @Override
  @Transactional(readOnly = true)
  public ArticleDetailsResponseDto getById(Long id, Long userId) {

    Article article = findById(id);
    return articleMapper.toDetailsDto(article, !articleVoteHandler.hasVoted(userId, id));
  }

  @Override
  @Transactional
  public void deleteArticle(Long articleId, Long userId) {
    Article article = findById(articleId);

    Mentor currentMentor = userFinder.findMentorById(userId);

    if (!article.getAuthor().getId().equals(currentMentor.getId())) {
      throw new AccessDeniedException("Вы можете удалять только свои статьи");
    }
    articleRepository.delete(article);
  }

  @Override
  @Transactional
  public ChangedRankResponseDto changeArticleRank(Long articleId, boolean like, Long userId) {
    return (new ChangedRankResponseDto(
        votingService.vote(articleId, userId, like, articleVoteHandler)));
  }

  @Override
  @Transactional(readOnly = true)
  public List<ArticleSummaryResponseDto> findArticlesByAuthor(Long authorId) {
    List<Article> articles = articleRepository.findAllByAuthorId(authorId);
    return articleMapper.toSummaryDtoList(articles);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(value = "popularArticles")
  public List<ArticleSummaryResponseDto> findPopularArticles() {
    List<Article> articles = articleRepository.findTop3ByOrderByRankDesc();
    return articleMapper.toSummaryDtoList(articles);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ArticleSummaryResponseDto> findArticles(
      Long specialityId, String query, Pageable pageable) {

    Specification<Article> specification = Specification.not(null);

    if (specialityId != null) {
      specification = specification.and(ArticleSpecifications.hasSpeciality(specialityId));
    }

    if (query != null && !query.isBlank()) {
      if (query.length() > 250) {
        throw new IllegalStateException("Сократите строку поиска до 250 символов");
      }
      specification = specification.and(ArticleSpecifications.titleOrContentContains(query));
    }
    Page<Article> articlePages = articleRepository.findAll(specification, pageable);

    return articlePages.map(articleMapper::toSummaryDto);
  }

  private Article findById(Long articleId) {
    return articleRepository
        .findById(articleId)
        .orElseThrow(() -> new EntityNotFoundException("Статья не найдена"));
  }
}
