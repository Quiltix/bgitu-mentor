package com.bgitu.mentor.article.data.model;

import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.model.Speciality;
import com.bgitu.mentor.vote.data.model.Votable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Article implements Votable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 5000)
    private String content;

    private String imageUrl;

    private int rank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speciality_id")
    private Speciality speciality;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Mentor author;  // односторонняя связь

    @Override
    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
