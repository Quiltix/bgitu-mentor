package com.bgitu.mentor.article.data.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class ArticleSummaryResponseDto {
    private Long id;
    private String title;
    private String shortContent;
    private String imageUrl;
    private int rank;
    private String specialityName;
    private String authorFullName;
}

