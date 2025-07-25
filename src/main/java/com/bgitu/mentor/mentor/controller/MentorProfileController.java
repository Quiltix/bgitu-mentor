package com.bgitu.mentor.mentor.controller;


import com.bgitu.mentor.article.dto.ArticleShortDto;
import com.bgitu.mentor.common.dto.PersonalInfoDto;
import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
import com.bgitu.mentor.mentor.dto.CardMentorDto;
import com.bgitu.mentor.mentor.dto.UpdateMentorCardDto;
import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.mentor.service.MentorService;
import com.bgitu.mentor.student.dto.StudentCardDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Mentor Profile", description = "Методы для взаимодействия с профилем ментора")
@RequestMapping("/api/profiles/mentor")
public class MentorProfileController {

    private final MentorService mentorService;

    @Operation(summary = "Получение карточки ментора", description = "Доступно только для роли MENTOR. Возвращает полную информацию по текущему пользователю.")
    @PreAuthorize("hasRole('MENTOR')")
    @GetMapping("/summary")
    public ResponseEntity<CardMentorDto> getCardMentor(Authentication authentication) {
        return ResponseEntity.ok(new CardMentorDto(mentorService.getByAuth(authentication)));
    }

    @Operation(summary = "Обновление карточки ментора", description = "Доступно только для роли MENTOR. Позволяет редактировать описание, направление и аватар.")
    @PreAuthorize("hasRole('MENTOR')")
    @PatchMapping(value = "/summary", consumes = "multipart/form-data")
    public ResponseEntity<CardMentorDto> updateMentorCard(
            Authentication authentication,
            @RequestPart("card") UpdateMentorCardDto dto,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile
    ) {

        Mentor updated = mentorService.updateCard(authentication, dto, avatarFile);
        return ResponseEntity.ok(new CardMentorDto(updated));
    }

    @Operation(summary = "Обновление профиля ментора", description = "Доступно только для роли MENTOR. Обновляет имя, фамилию, пароль и email.")
    @PreAuthorize("hasRole('MENTOR')")
    @PatchMapping("/profile")
    public ResponseEntity<PersonalInfoDto> updateMentorProfile(
            Authentication authentication,
            @RequestBody @Valid UpdatePersonalInfo dto
    ) {

        Mentor updated = mentorService.updateProfile(authentication, dto);
        return ResponseEntity.ok(new PersonalInfoDto(updated));

    }

    @Operation(summary = "Получение моего профиля", description = "MENTOR. Возвращает профиль по авторизации")
    @PreAuthorize("hasRole('MENTOR')")
    @GetMapping("/profile")
    public ResponseEntity<PersonalInfoDto> getMentorProfile(Authentication authentication) {
        return ResponseEntity.ok(new PersonalInfoDto(mentorService.getByAuth(authentication)));
    }

    @Operation(summary = "Получение всех статей ментора", description = "Доступно для роли MENTOR")
    @PreAuthorize("hasRole('MENTOR')")
    @GetMapping("/articles")
    public List<ArticleShortDto> getArticlesByMentor(Authentication authentication) {
        return mentorService.getMentorArticles(authentication);
    }

    @Operation(summary = "Получение всех студентов ментора", description = "Доступно для роли MENTOR")
    @PreAuthorize("hasRole('MENTOR')")
    @GetMapping("/students")
    public List<StudentCardDto> getStudents(Authentication authentication) {
        return mentorService.getAllStudentsForMentor(authentication);
    }
}
