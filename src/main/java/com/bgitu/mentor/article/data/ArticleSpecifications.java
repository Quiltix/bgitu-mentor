package com.bgitu.mentor.article.data;


import com.bgitu.mentor.article.data.model.Article;
import com.bgitu.mentor.mentor.data.model.Speciality;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public final class ArticleSpecifications {

    private ArticleSpecifications() {
        throw new IllegalStateException("Utility class");
    }

    public static Specification<Article> hasSpeciality(Long specialityId){
        return  (root, query, criteriaBuilder) ->
        {
            Join<Article, Speciality> specialityJoin = root.join("speciality");
            return criteriaBuilder.equal(specialityJoin.get("id"),specialityId);
        };
    }

    public static Specification<Article> titleOrContentContains(String query) {
        String likePattern = "%" + query.toLowerCase() + "%";
        return (root, query1, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likePattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), likePattern)
                );
    }
}

