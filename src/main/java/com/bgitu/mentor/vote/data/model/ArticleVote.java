package com.bgitu.mentor.vote.data.model;

import com.bgitu.mentor.article.data.model.Article;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "article_vote",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"article_id", "mentor_id"}),
                @UniqueConstraint(columnNames = {"article_id", "student_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleVote extends BaseVote {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

}
