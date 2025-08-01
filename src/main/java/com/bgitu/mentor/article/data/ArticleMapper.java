package com.bgitu.mentor.article.data;


import com.bgitu.mentor.article.data.dto.ArticleDetailsResponseDto;
import com.bgitu.mentor.article.data.dto.ArticleSummaryResponseDto;
import com.bgitu.mentor.article.data.model.Article;
import com.bgitu.mentor.mentor.data.model.Mentor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ArticleMapper {


    @Mapping(source = "article.speciality.name", target = "specialityName")
    @Mapping(source = "article.author", target = "authorFullName", qualifiedByName = "authorToFullName")
    @Mapping(source = "canVote", target = "canVote")
    ArticleDetailsResponseDto toDetailsDto(Article article, Boolean canVote);

    @Mapping(source = "speciality.name", target = "specialityName")
    @Mapping(source = "content", target = "shortContent", qualifiedByName = "contentToShort")
    @Mapping(source = "author", target = "authorFullName", qualifiedByName = "authorToFullName")
    ArticleSummaryResponseDto toSummaryDto(Article article);


    default ArticleDetailsResponseDto toDetailsDto(Article article) {
        return toDetailsDto(article, null);
    }

    @Named("contentToShort")
    default String contentToShort(String content){

        if (content != null) {
            return  content.length() > 300
                    ? content.substring(0, 297) + "..."
                    : content;
        }
        return null;

    }


    @Named("authorToFullName")
    default String authorToFullName(Mentor author){
        if (author == null){
            return null;
        }
        return author.getFirstName() + " " + author.getLastName();
    }

}
