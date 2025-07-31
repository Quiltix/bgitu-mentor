package com.bgitu.mentor.mentor.controller;


import com.bgitu.mentor.article.data.dto.ArticleSummaryResponseDto;
import com.bgitu.mentor.common.SecurityUtils;
import com.bgitu.mentor.common.dto.UserCredentialsResponseDto;
import com.bgitu.mentor.common.dto.UserCredentialsUpdateRequestDto;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.mentor.data.dto.MentorUpdateRequestDto;
import com.bgitu.mentor.mentor.service.MentorProfileService;
import com.bgitu.mentor.student.data.dto.StudentDetailsResponseDto;
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

    private final MentorProfileService mentorService;

    @Operation(summary = "Получение карточки ментора", description = "Доступно только для роли MENTOR. Возвращает полную информацию по текущему пользователю.")
    @PreAuthorize("hasRole('MENTOR')")
    @GetMapping()
    public ResponseEntity<MentorDetailsResponseDto> getCardMentor(Authentication authentication) {
        Long mentorId = SecurityUtils.getCurrentUserId(authentication);
        return ResponseEntity.ok(mentorService.getMyCard(mentorId));
    }

    @Operation(summary = "Обновление карточки ментора", description = "Доступно только для роли MENTOR. Позволяет редактировать описание, направление и аватар.")
    @PreAuthorize("hasRole('MENTOR')")
    @PatchMapping(consumes = "multipart/form-data")
    public ResponseEntity<MentorDetailsResponseDto> updateMentorCard(
            Authentication authentication,
            @RequestPart("card") MentorUpdateRequestDto dto,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile
    ) {


        Long mentorId = SecurityUtils.getCurrentUserId(authentication);
        return ResponseEntity.ok(mentorService.updateCard(mentorId, dto, avatarFile));
    }

    @Operation(summary = "Обновление профиля ментора", description = "Доступно только для роли MENTOR. Обновляет имя, фамилию, пароль и email.")
    @PreAuthorize("hasRole('MENTOR')")
    @PatchMapping("/settings")
    public ResponseEntity<UserCredentialsResponseDto> updateMentorProfile(
            Authentication authentication,
            @RequestBody @Valid UserCredentialsUpdateRequestDto dto
    ) {

        Long mentorId = SecurityUtils.getCurrentUserId(authentication);
        return ResponseEntity.ok(mentorService.updateProfile(mentorId, dto));

    }

    @Operation(summary = "Получение моего профиля", description = "MENTOR. Возвращает профиль по авторизации")
    @PreAuthorize("hasRole('MENTOR')")
    @GetMapping("/settings")
    public ResponseEntity<UserCredentialsResponseDto> getMentorProfile(Authentication authentication) {
        Long mentorId = SecurityUtils.getCurrentUserId(authentication);
        return ResponseEntity.ok(mentorService.getPersonalInfo(mentorId));
    }

    @Operation(summary = "Получение всех статей ментора", description = "Доступно для роли MENTOR")
    @PreAuthorize("hasRole('MENTOR')")
    @GetMapping("/articles")
    public List<ArticleSummaryResponseDto> getArticlesByMentor(Authentication authentication) {
        Long mentorId = SecurityUtils.getCurrentUserId(authentication);
        return mentorService.getMyArticles(mentorId);
    }

    @Operation(summary = "Получение всех студентов ментора", description = "Доступно для роли MENTOR")
    @PreAuthorize("hasRole('MENTOR')")
    @GetMapping("/students")
    public List<StudentDetailsResponseDto> getStudents(Authentication authentication) {
        Long mentorId = SecurityUtils.getCurrentUserId(authentication);
        return mentorService.getMyStudents(mentorId);
    }

    @DeleteMapping("/students/{studentId}")
    @PreAuthorize("hasRole('MENTOR')")
    @Operation(summary = "Прекратить менторство со студентом", description = "Позволяет ментору отказаться от своего студента.")

    public ResponseEntity<Void> terminateMentorshipWithStudent(
            Authentication authentication,
            @PathVariable Long studentId) {
        Long mentorId = SecurityUtils.getCurrentUserId(authentication);
        mentorService.terminateMentorshipWithStudent(mentorId, studentId);
        return ResponseEntity.noContent().build();
    }
}
