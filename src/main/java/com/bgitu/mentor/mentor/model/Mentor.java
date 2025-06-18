package com.bgitu.mentor.mentor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "mentor")
public class Mentor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String firstName;

    private String lastName;

    private String description;

    private Byte rank;



}
