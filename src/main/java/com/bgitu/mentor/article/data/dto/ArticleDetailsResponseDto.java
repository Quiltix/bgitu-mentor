package com.bgitu.mentor.article.data.dto;


import com.bgitu.mentor.article.data.model.Article;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleDetailsResponseDto {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private int rank;
    private String specialityName;
    private String authorFullName;
    private Boolean canVote;

    public ArticleDetailsResponseDto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.imageUrl = article.getImageUrl();
        this.rank = article.getRank();
        this.specialityName = article.getSpeciality().getName();
        this.authorFullName = article.getAuthor().getFirstName() + " " + article.getAuthor().getLastName();
    }

    public ArticleDetailsResponseDto(Article article, Boolean canVote) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.imageUrl = article.getImageUrl();
        this.rank = article.getRank();
        this.specialityName = article.getSpeciality().getName();
        this.authorFullName = article.getAuthor().getFirstName() + " " + article.getAuthor().getLastName();
        this.canVote = canVote;
    }
}
