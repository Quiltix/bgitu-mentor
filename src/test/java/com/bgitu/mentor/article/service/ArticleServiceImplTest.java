package com.bgitu.mentor.article.service;

import com.bgitu.mentor.article.data.ArticleMapper;
import com.bgitu.mentor.article.data.dto.ArticleCreateRequestDto;
import com.bgitu.mentor.article.data.dto.ArticleDetailsResponseDto;
import com.bgitu.mentor.article.data.dto.ArticleSummaryResponseDto;
import com.bgitu.mentor.article.data.model.Article;
import com.bgitu.mentor.article.data.repository.ArticleRepository;
import com.bgitu.mentor.file.service.FileStorageService;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.speciality.data.model.Speciality;
import com.bgitu.mentor.speciality.service.SpecialityService;
import com.bgitu.mentor.user.service.UserFinder;
import com.bgitu.mentor.vote.service.ArticleVoteHandler;
import com.bgitu.mentor.vote.service.VotingService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceImplTest {

  @Mock private ArticleRepository articleRepository;
  @Mock private UserFinder userFinder;
  @Mock private SpecialityService specialityService;
  @Mock private FileStorageService fileStorageService;
  @Mock private VotingService votingService;
  @Mock private ArticleVoteHandler articleVoteHandler;
  @Mock private ArticleMapper articleMapper;

  @InjectMocks private ArticleServiceImpl articleService;

  @DisplayName("createArticle | Should create article with valid data and image")
  @Test
  void createArticle_createsArticle_withValidDataAndImage() {
    Long authorId = 1L;
    Long specialityId = 2L;
    ArticleCreateRequestDto dto = new ArticleCreateRequestDto();
    dto.setTitle("Test Title");
    dto.setContent("Test Content");
    dto.setSpecialityId(specialityId);
    MultipartFile image =
        new MockMultipartFile("image", "image.jpg", "image/jpeg", "image-bytes".getBytes());

    Mentor author = new Mentor();
    Speciality speciality = new Speciality();
    Article savedArticle = new Article();
    savedArticle.setId(1L);

    when(userFinder.findMentorById(authorId)).thenReturn(author);
    when(specialityService.getById(specialityId)).thenReturn(speciality);
    when(fileStorageService.store(image, "articles")).thenReturn("stored/image.jpg");
    when(articleRepository.save(any(Article.class))).thenReturn(savedArticle);
    when(articleMapper.toDetailsDto(savedArticle)).thenReturn(new ArticleDetailsResponseDto());

    ArticleDetailsResponseDto result = articleService.createArticle(authorId, dto, image);

    verify(userFinder, times(1)).findMentorById(authorId);
    verify(specialityService, times(1)).getById(specialityId);
    verify(fileStorageService, times(1)).store(image, "articles");
    verify(articleRepository, times(1)).save(any(Article.class));
    verify(articleMapper, times(1)).toDetailsDto(savedArticle);
    assertNotNull(result);
  }

  @DisplayName("createArticle | Should create article without image")
  @Test
  void createArticle_createsArticle_withoutImage() {
    Long authorId = 1L;
    Long specialityId = 2L;
    ArticleCreateRequestDto dto = new ArticleCreateRequestDto();
    dto.setTitle("Test Title");
    dto.setContent("Test Content");
    dto.setSpecialityId(specialityId);

    Mentor author = new Mentor();
    Speciality speciality = new Speciality();
    Article savedArticle = new Article();
    savedArticle.setId(1L);

    when(userFinder.findMentorById(authorId)).thenReturn(author);
    when(specialityService.getById(specialityId)).thenReturn(speciality);
    when(articleRepository.save(any(Article.class))).thenReturn(savedArticle);
    when(articleMapper.toDetailsDto(savedArticle)).thenReturn(new ArticleDetailsResponseDto());

    ArticleDetailsResponseDto result = articleService.createArticle(authorId, dto, null);

    verify(userFinder, times(1)).findMentorById(authorId);
    verify(specialityService, times(1)).getById(specialityId);
    verify(articleRepository, times(1)).save(any(Article.class));
    verify(articleMapper, times(1)).toDetailsDto(savedArticle);
    verifyNoInteractions(fileStorageService);
    assertNotNull(result);
  }

  @DisplayName("createArticle | Should throw exception if author not found")
  @Test
  void createArticle_throwsException_whenAuthorNotFound() {
    Long authorId = 1L;
    ArticleCreateRequestDto dto = new ArticleCreateRequestDto();
    MultipartFile image =
        new MockMultipartFile("image", "image.jpg", "image/jpeg", "image-bytes".getBytes());

    when(userFinder.findMentorById(authorId))
        .thenThrow(new EntityNotFoundException("Author not found"));

    assertThrows(
        EntityNotFoundException.class, () -> articleService.createArticle(authorId, dto, image));

    verify(userFinder, times(1)).findMentorById(authorId);
    verifyNoInteractions(specialityService, fileStorageService, articleRepository, articleMapper);
  }

  @DisplayName("createArticle | Should throw exception if speciality not found")
  @Test
  void createArticle_throwsException_whenSpecialityNotFound() {
    Long authorId = 1L;
    Long specialityId = 2L;
    ArticleCreateRequestDto dto = new ArticleCreateRequestDto();
    dto.setSpecialityId(specialityId);
    MultipartFile image =
        new MockMultipartFile("image", "image.jpg", "image/jpeg", "image-bytes".getBytes());

    Mentor author = new Mentor();

    when(userFinder.findMentorById(authorId)).thenReturn(author);
    when(specialityService.getById(specialityId))
        .thenThrow(new EntityNotFoundException("Speciality not found"));

    assertThrows(
        EntityNotFoundException.class, () -> articleService.createArticle(authorId, dto, image));

    verify(userFinder, times(1)).findMentorById(authorId);
    verify(specialityService, times(1)).getById(specialityId);
    verifyNoInteractions(fileStorageService, articleRepository, articleMapper);
  }

  @DisplayName("deleteArticle | Should delete article when user is the author")
  @Test
  void deleteArticle_deletesArticle_whenUserIsAuthor() {
    Long articleId = 1L;
    Long userId = 2L;

    Article article = new Article();
    Mentor author = new Mentor();
    author.setId(userId);
    article.setAuthor(author);

    when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
    when(userFinder.findMentorById(userId)).thenReturn(author);

    articleService.deleteArticle(articleId, userId);

    verify(articleRepository, times(1)).findById(articleId);
    verify(userFinder, times(1)).findMentorById(userId);
    verify(articleRepository, times(1)).delete(article);
  }

  @DisplayName("deleteArticle | Should throw exception when article does not exist")
  @Test
  void deleteArticle_throwsException_whenArticleDoesNotExist() {
    Long articleId = 1L;
    Long userId = 2L;

    when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

    assertThrows(
        EntityNotFoundException.class, () -> articleService.deleteArticle(articleId, userId));

    verify(articleRepository, times(1)).findById(articleId);
    verifyNoInteractions(userFinder);
  }

  @DisplayName("deleteArticle | Should throw exception when user is not the author")
  @Test
  void deleteArticle_throwsException_whenUserIsNotAuthor() {
    Long articleId = 1L;
    Long userId = 2L;
    Long anotherUserId = 3L;

    Article article = new Article();
    Mentor author = new Mentor();
    author.setId(anotherUserId);
    article.setAuthor(author);

    Mentor currentMentor = new Mentor();
    currentMentor.setId(userId);

    when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
    when(userFinder.findMentorById(userId)).thenReturn(currentMentor);

    assertThrows(
        AccessDeniedException.class, () -> articleService.deleteArticle(articleId, userId));

    verify(articleRepository, times(1)).findById(articleId);
    verify(userFinder, times(1)).findMentorById(userId);
    verify(articleRepository, never()).delete(article);
  }

  @DisplayName(
      "findArticles | Should return paginated articles when specialityId and query are null")
  @Test
  void findArticles_returnsPaginatedArticles_whenSpecialityIdAndQueryAreNull() {
    Pageable pageable = mock(Pageable.class);
    Page<Article> articlePage = mock(Page.class);
    when(articleRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(articlePage);
    when(articlePage.map(any())).thenReturn(Page.empty());

    Page<ArticleSummaryResponseDto> result = articleService.findArticles(null, null, pageable);

    verify(articleRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    assertNotNull(result);
  }

  @DisplayName("findArticles | Should return paginated articles when specialityId is provided")
  @Test
  void findArticles_returnsPaginatedArticles_whenSpecialityIdIsProvided() {
    Long specialityId = 1L;
    Pageable pageable = mock(Pageable.class);
    Page<Article> articlePage = mock(Page.class);
    when(articleRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(articlePage);
    when(articlePage.map(any())).thenReturn(Page.empty());

    Page<ArticleSummaryResponseDto> result =
        articleService.findArticles(specialityId, null, pageable);

    verify(articleRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    assertNotNull(result);
  }

  @DisplayName("findArticles | Should return paginated articles when query is provided")
  @Test
  void findArticles_returnsPaginatedArticles_whenQueryIsProvided() {
    String query = "test";
    Pageable pageable = mock(Pageable.class);
    Page<Article> articlePage = mock(Page.class);
    when(articleRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(articlePage);
    when(articlePage.map(any())).thenReturn(Page.empty());

    Page<ArticleSummaryResponseDto> result = articleService.findArticles(null, query, pageable);

    verify(articleRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    assertNotNull(result);
  }

  @DisplayName("findArticles | Should throw exception when query length exceeds 250 characters")
  @Test
  void findArticles_throwsException_whenQueryLengthExceedsLimit() {
    String longQuery = "a".repeat(251);
    Pageable pageable = mock(Pageable.class);

    assertThrows(
        IllegalStateException.class, () -> articleService.findArticles(null, longQuery, pageable));
    verifyNoInteractions(articleRepository);
  }

  @DisplayName("findById | Should find article when it exists")
  @Test
  void findById_shouldReturnsArticle_whenExists() {
    Article fakeArticle = new Article();
    fakeArticle.setTitle("test");
    fakeArticle.setId(1L);
    ArticleDetailsResponseDto fakeResult = new ArticleDetailsResponseDto();
    fakeResult.setId(1L);
    fakeResult.setTitle("test");

    when(articleMapper.toDetailsDto(fakeArticle, 0)).thenReturn(fakeResult);
    when(articleRepository.findById(1L)).thenReturn(Optional.of(fakeArticle));
    when(articleVoteHandler.getResultVote(2L, 1L)).thenReturn(0);

    ArticleDetailsResponseDto result = articleService.getById(1L, 2L);

    assertNotNull(result);
    assertEquals(fakeResult, result);
  }
}
