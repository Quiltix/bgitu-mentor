package com.bgitu.mentor.article.data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleCreateRequestDto {

  @Size(max = 250, message = "Сократите название до 250 символов")
  @NotBlank(message = "У статьи должно быть название")
  @NotNull
  @Schema(description = "Название статьи", example = "Основы Java")
  private String title;

  @Size(max = 5000, message = "Сократите содержание статьи до 5000 символов")
  @NotBlank(message = "Статья должна содержать информацию")
  @NotNull
  @Schema(description = "Содержание статьи до 5к символов", example = "Java является популярным...")
  private String content;

  private Long specialityId;
}
