package com.bgitu.mentor.article.controller;

import com.bgitu.mentor.article.data.dto.ArticleCreateRequestDto;
import com.bgitu.mentor.article.data.dto.ArticleDetailsResponseDto;
import com.bgitu.mentor.article.data.dto.ArticleSummaryResponseDto;
import com.bgitu.mentor.article.service.ArticleService;
import com.bgitu.mentor.auth.security.SecurityUtils;
import com.bgitu.mentor.exception.dto.ErrorResponseDto;
import com.bgitu.mentor.vote.data.dto.ChangedRankResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Tag(name = "Article", description = "Методы для взаимодействия со статьями")
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {
  private final ArticleService articleService;

  @PreAuthorize("hasRole('MENTOR')")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Создание статьи", description = "Позволяет ментору опубликовать статью")
  @ApiResponses({
    @ApiResponse(
        responseCode = "201",
        description = "Статья создана",
        content = @Content(schema = @Schema(implementation = ArticleDetailsResponseDto.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Специальность с таким id не найдена",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    @ApiResponse(
        responseCode = "401",
        description = "Пользователь не аутентифицирован",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Пользователь, который создает, не найден(такое в теории быть не может)",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Внутренняя ошибка сервера(связана скорее всего с сохранением файла)",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  public ResponseEntity<ArticleDetailsResponseDto> createArticle(
      Authentication auth,
      @RequestPart("data") @Valid ArticleCreateRequestDto dto,
      @RequestPart(value = "image", required = false) MultipartFile image) {
    Long authorId = SecurityUtils.getCurrentUserId(auth);

    ArticleDetailsResponseDto createdArticle = articleService.createArticle(authorId, dto, image);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdArticle.getId())
            .toUri();

    return ResponseEntity.created(location).body(createdArticle);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/{id}")
  @Operation(
      summary = "Получить статью по ID",
      description =
          "Возвращает полную информацию о статье включая возможность голосования для текущего пользователя")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Статья найдена",
        content = @Content(schema = @Schema(implementation = ArticleDetailsResponseDto.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Неверный ID статьи",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    @ApiResponse(
        responseCode = "401",
        description = "Пользователь не аутентифицирован",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Статья не найдена",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Внутренняя ошибка сервера",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  public ArticleDetailsResponseDto getArticleById(
      @Parameter(description = "ID статьи", example = "15") @PathVariable Long id,
      Authentication authentication) {
    Long userId = SecurityUtils.getCurrentUserId(authentication);
    return articleService.getById(id, userId);
  }

  @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
  @GetMapping
  @Operation(
      summary = "Получить список статей с фильтрацией и пагинацией",
      description =
          "Возвращает пагинированный список статей. "
              + "Фильтры: `specialityId`, `query`. "
              + "Сортировка: `sort=rank,desc`. "
              + "Пагинация: `page`, `size`.")
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Статьи найдены"),
    @ApiResponse(
        responseCode = "400",
        description = "Неверные параметры запроса",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    @ApiResponse(
        responseCode = "401",
        description = "Пользователь не аутентифицирован",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    @ApiResponse(
        responseCode = "403",
        description = "Недостаточно прав",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Внутренняя ошибка сервера",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  public Page<ArticleSummaryResponseDto> findArticles(
      @Parameter(description = "ID специализации для фильтрации", example = "1")
          @RequestParam(required = false)
          Long specialityId,
      @Parameter(description = "Строка для поиска по заголовку и содержанию", example = "Java")
          @RequestParam(required = false)
          String query,
      @Parameter(description = "Параметры пагинации и сортировки") Pageable pageable) {

    return articleService.findArticles(specialityId, query, pageable);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('MENTOR')")
  @Operation(summary = "Удалить статью", description = "Удаляет статью, если вы её автор")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiResponses({
    @ApiResponse(responseCode = "200", description = "Статья удалена"),
    @ApiResponse(
        responseCode = "403",
        description = "Пользователь не автор статьи",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Неверный ID статьи",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    @ApiResponse(
        responseCode = "401",
        description = "Пользователь не аутентифицирован",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Статья не найдена",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Внутренняя ошибка сервера",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  public void deleteArticle(@PathVariable Long id, Authentication authentication) {
    Long userId = SecurityUtils.getCurrentUserId(authentication);

    articleService.deleteArticle(id, userId);
  }

  @PreAuthorize("isAuthenticated()")
  @PostMapping("/{id}/vote")
  @Operation(
      summary = "Оценить статью",
      description = "Лайк или дизлайк статьи. true = лайк, false = дизлайк")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Статья лайкнута",
        content = @Content(schema = @Schema(implementation = ChangedRankResponseDto.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Статью уже лайкнули",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    @ApiResponse(
        responseCode = "404",
        description = "Пользователь, который голосует, не найден(такое в теории быть не может)",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    @ApiResponse(
        responseCode = "401",
        description = "Пользователь не аутентифицирован",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Ошибка сервера",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  @ResponseStatus(HttpStatus.OK)
  public ChangedRankResponseDto voteArticle(
      @PathVariable Long id, @RequestParam boolean like, Authentication authentication) {
    Long userId = SecurityUtils.getCurrentUserId(authentication);
    return articleService.changeArticleRank(id, like, userId);
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/popular")
  @Operation(
      summary = "Получить популярный статьи",
      description = "Возвращает топ 3 статьи по рейтингу")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Статьи найдены",
        content =
            @Content(
                array =
                    @ArraySchema(
                        schema = @Schema(implementation = ArticleSummaryResponseDto.class)))),
    @ApiResponse(
        responseCode = "401",
        description = "Пользователь не аутентифицирован",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    @ApiResponse(
        responseCode = "500",
        description = "Ошибка сервера",
        content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  })
  public List<ArticleSummaryResponseDto> getPopularArticles() {
    return articleService.findPopularArticles();
  }
}
