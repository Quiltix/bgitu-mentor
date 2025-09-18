package com.bgitu.mentor.article.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Детальная информация о статье")
public class ArticleDetailsResponseDto {

  @Schema(description = "ID статьи", example = "15")
  private Long id;

  @Schema(description = "Заголовок статьи", example = "Основы Java")
  private String title;

  @Schema(description = "Содержание статьи", example = "Java является популярным языком...")
  private String content;

  @Schema(description = "URL изображения статьи", example = "https://example.com/image.jpg")
  private String imageUrl;

  @Schema(description = "Рейтинг статьи", example = "42")
  private Integer rank;

  @Schema(description = "Название специализации", example = "Backend разработка")
  private String specialityName;

  @Schema(description = "Полное имя автора", example = "Иван Петров")
  private String authorFullName;

  @Schema(description = "Может ли текущий пользователь голосовать за статью")
  private Boolean canVote;
}
