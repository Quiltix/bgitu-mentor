package com.bgitu.mentor.arcticle.model;

import com.bgitu.mentor.mentor.model.Mentor;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;


    @Column(length = 5000)
    private String content;

    private String imageUrl;

    @ElementCollection
    @CollectionTable(name = "article_technology", joinColumns = @JoinColumn(name = "article_id"))
    @Column(name = "technology")
    private List<String> technologies; // JAva Spring QA SQL .split(" )

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Mentor author;  // односторонняя связь
}
