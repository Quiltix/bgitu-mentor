package com.bgitu.mentor.user.data.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BaseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email( message = "Email должен быть корректным")
    @NotBlank(message = "Email должен быть не пустым")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Password должен быть не пустым")
    private String password;

    private String firstName;
    private String lastName;

    @Column(length = 2000)
    private String description;

    private String avatarUrl;
    private String vkUrl;
    private String telegramUrl;
}