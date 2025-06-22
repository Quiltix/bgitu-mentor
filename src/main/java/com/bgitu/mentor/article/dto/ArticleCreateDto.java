package com.bgitu.mentor.article.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleCreateDto {

    @Size(min = 250, message = "Сократите название до 250 символов")
    @NotBlank(message = "У статьи должно быть название")
    private String title;

    @Size(min = 5000, message = "Сократите содержание статьи до 5000 символов")
    @NotBlank(message = "Статья должна содержать информацию")
    private String content;


    private Long specialityId;
}
