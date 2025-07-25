package com.bgitu.mentor.article.controller;

import com.bgitu.mentor.article.dto.ArticleCreateDto;
import com.bgitu.mentor.article.dto.ArticleResponseDto;
import com.bgitu.mentor.article.dto.ArticleShortDto;
import com.bgitu.mentor.article.service.ArticleService;
import com.bgitu.mentor.common.dto.MessageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Tag(name = "Article", description = "Методы для взаимодействия со статьями")
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PreAuthorize("hasRole('MENTOR')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Создание статьи", description = "Позволяет ментору опубликовать статью")
    public ResponseEntity<ArticleResponseDto> createArticle(
            Authentication auth,
            @RequestPart("data")  @Valid ArticleCreateDto dto,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return ResponseEntity.ok(articleService.createArticle(auth, dto, image));
    }


    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    @GetMapping("/{id}")
    @Operation(summary = "Получить статью", description = "Получить статью по её ID")
    public ResponseEntity<ArticleResponseDto> getArticleById(@PathVariable Long id) {
        return ResponseEntity.ok(articleService.getById(id));
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    @GetMapping
    @Operation(summary = "Получить все статьи", description = "Возвращает статьи, отсортированные по убыванию ранга. Можно фильтровать по специальности.")
    public ResponseEntity<List<ArticleShortDto>> getAllArticles(
            @RequestParam(name = "specialityId", required = false) Long specialityId
    ) {
        return ResponseEntity.ok(articleService.getAllArticles(Optional.ofNullable(specialityId)));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MENTOR')")
    @Operation(summary = "Удалить статью", description = "Удаляет статью, если вы её автор")
    public ResponseEntity<MessageDto> deleteArticle(@PathVariable Long id, Authentication authentication) {
        articleService.deleteArticle(id, authentication);
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
        articleService.changeArticleRank(id, like,authentication);
        return ResponseEntity.ok(new MessageDto(like ? "Статья лайкнута" : "Статья дизлайкнута"));
    }



    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    @GetMapping("/top")
    @Operation(summary = "Получить топ-3 статьи", description = "Возвращает 3 лучшие статьи по рейтингу")
    public ResponseEntity<List<ArticleShortDto>> getTop3Articles() {
        return ResponseEntity.ok(articleService.getTop3Articles());
    }

    @Operation(summary = "Поиск статей", description = "Доступно для всех ролей. Ищет по названию и содержанию статьи.")
    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    @GetMapping("/search")
    public ResponseEntity<List<ArticleShortDto>> searchArticles(@RequestParam String query) {
        return ResponseEntity.ok(articleService.searchArticles(query));
    }





}

