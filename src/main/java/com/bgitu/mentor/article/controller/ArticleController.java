package com.bgitu.mentor.article.controller;

import com.bgitu.mentor.article.data.dto.ArticleCreateRequestDto;
import com.bgitu.mentor.article.data.dto.ArticleDetailsResponseDto;
import com.bgitu.mentor.article.data.dto.ArticleSummaryResponseDto;
import com.bgitu.mentor.article.service.ArticleService;
import com.bgitu.mentor.common.SecurityUtils;
import com.bgitu.mentor.common.dto.MessageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "Article", description = "Методы для взаимодействия со статьями")
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PreAuthorize("hasRole('MENTOR')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Создание статьи", description = "Позволяет ментору опубликовать статью")
    public ResponseEntity<ArticleDetailsResponseDto> createArticle(
            Authentication auth,
            @RequestPart("data")  @Valid ArticleCreateRequestDto dto,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        Long authorId = SecurityUtils.getCurrentUserId(auth);
        return ResponseEntity.ok(articleService.createArticle(authorId, dto, image));
    }


    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    @GetMapping("/{id}")
    @Operation(summary = "Получить статью", description = "Получить статью по её ID")
    public ResponseEntity<ArticleDetailsResponseDto> getArticleById(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getById(id));
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    @GetMapping
    @Operation(
            summary = "Получить список статей с фильтрацией и пагинацией",
            description = "Возвращает пагинированный список статей. " +
                    "Фильтры: `specialityId`, `query`. " +
                    "Сортировка: `sort=rank,desc`. " +
                    "Пагинация: `page`, `size`."
    )
    public ResponseEntity<Page<ArticleSummaryResponseDto>> findArticles(
            @RequestParam(required = false) Long specialityId,
            @RequestParam(required = false) String query,
            Pageable pageable
    ) {
        return ResponseEntity.ok(articleService.findArticles(specialityId, query, pageable));

    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MENTOR')")
    @Operation(summary = "Удалить статью", description = "Удаляет статью, если вы её автор")
    public ResponseEntity<MessageDto> deleteArticle(@PathVariable Long id, Authentication authentication) {
        Long userId = SecurityUtils.getCurrentUserId(authentication);

        articleService.deleteArticle(id, userId);
        return ResponseEntity.ok(new MessageDto("Статья удалена"));
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    @PostMapping("/{id}/vote")
    @Operation(summary = "Оценить статью", description = "Лайк или дизлайк статьи. true = лайк, false = дизлайк")
    public ResponseEntity<MessageDto> voteArticle(
            @PathVariable Long id,
            @RequestParam boolean like,
            Authentication authentication
    ) {
        Long userId = SecurityUtils.getCurrentUserId(authentication);
        articleService.changeArticleRank(id, like, userId);
        return ResponseEntity.ok(new MessageDto(like ? "Статья лайкнута" : "Статья дизлайкнута"));
    }

}

