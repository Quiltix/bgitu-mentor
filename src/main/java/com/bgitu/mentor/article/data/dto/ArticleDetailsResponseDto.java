package com.bgitu.mentor.article.data.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleDetailsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private Integer rank;
    private String specialityName;
    private String authorFullName;
    private Boolean canVote;
}
