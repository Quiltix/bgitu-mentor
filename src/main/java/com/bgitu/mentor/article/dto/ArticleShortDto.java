package com.bgitu.mentor.article.dto;

import com.bgitu.mentor.article.model.Article;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ArticleShortDto {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private int rank;
    private String specialityName;
    private String authorFullName;

    public ArticleShortDto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.imageUrl = article.getImageUrl();
        this.rank = article.getRank();
        this.specialityName = article.getSpeciality().getName();
        this.authorFullName = article.getAuthor().getFirstName() + " " + article.getAuthor().getLastName();
        String desc = article.getContent();
        if (desc != null) {
            this.content = desc.length() > 120
                    ? desc.substring(0, 117) + "..."
                    : desc;
        } else {
            this.content = "";
        }
    }
}

