package com.bgitu.mentor.article.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleCreateDto {

    @NotBlank(message = "У статьи должно быть название")
    private String title;

    @NotBlank(message = "Статья должна содержать информацию")
    private String content;


    private Long specialityId;
}
