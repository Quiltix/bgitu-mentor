package com.bgitu.mentor.article.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArticleSummaryResponseDto {
  @Schema(description = "Id статьи", example = "2")
  private Long id;

  @Schema(description = "Название статьи", example = "Основы Java")
  private String title;

  @Schema(
      description = "Укороченное содержание статьи",
      example = "Основы Java представляют собой...")
  private String shortContent;

  @Schema(description = "Путь к изображению статьи", example = "/images/3242.png")
  private String imageUrl;

  @Schema(description = "Рейтинг статьи", example = "11")
  private int rank;

  @Schema(description = "Название специальности ", example = "Java")
  private String specialityName;

  @Schema(description = "Имя и фамилия автора через пробел", example = "Иван Иванов")
  private String authorFullName;
}
