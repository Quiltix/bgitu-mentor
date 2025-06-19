package com.bgitu.mentor.arcticle.model;

import com.bgitu.mentor.mentor.model.Mentor;
import jakarta.persistence.*;

@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
//
//    @Column(length = 5000)
//    private String content;
//
//    private String imageUrl;
//
//    @ManyToOne
//    private Mentor mentor;
}
