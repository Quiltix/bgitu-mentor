package com.bgitu.mentor.article.data.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleCreateDto {

    @Size(max = 250, message = "Сократите название до 250 символов")
    @NotBlank(message = "У статьи должно быть название")
    @NotNull
    private String title;

    @Size(max = 5000, message = "Сократите содержание статьи до 5000 символов")
    @NotBlank(message = "Статья должна содержать информацию")
    @NotNull
    private String content;


    private Long specialityId;
}
