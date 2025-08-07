package com.bgitu.mentor.mentorship.controller;


import com.bgitu.mentor.auth.Role;
import com.bgitu.mentor.common.SecurityUtils;
import com.bgitu.mentor.mentorship.data.dto.ApplicationDecisionRequestDto;
import com.bgitu.mentor.mentorship.data.dto.ApplicationDetailsResponseDto;
import com.bgitu.mentor.mentorship.data.dto.ApplicationCreateRequestDto;
import com.bgitu.mentor.mentorship.data.model.ApplicationStatus;
import com.bgitu.mentor.mentorship.service.MentorshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@Tag(name = "Applications", description = "Управление заявками на менторство между студентами и менторами")
public class ApplicationController {

    private final MentorshipService mentorshipService;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping()
    @ApiResponse(responseCode = "201", description = "Заявка успешно создана")
    @Operation(summary = "Отправить заявку на менторство", description = "Позволяет студенту отправить заявку выбранному ментору")
    public ResponseEntity<ApplicationDetailsResponseDto> requestMentorship(Authentication authentication, @RequestBody @Valid ApplicationCreateRequestDto dto) {
        Long studentId = SecurityUtils.getCurrentUserId(authentication);
        ApplicationDetailsResponseDto responseDto =  mentorshipService.createApplication(studentId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PreAuthorize("hasRole('MENTOR')")
    @PatchMapping("/{applicationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Ответить на заявку", description = "Ментор обновляет статус существующей заявки (Принять/Отклонить)")
    public void respondToApplication(
            Authentication authentication,
            @PathVariable Long applicationId,
            @RequestBody @Valid ApplicationDecisionRequestDto dto) {
        Long mentorId = SecurityUtils.getCurrentUserId(authentication);
        mentorshipService.updateApplicationStatus(mentorId, applicationId, dto);
    }


    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Получить мои заявки (входящие для ментора, отправленные для студента)")
    public List<?> getMyApplications(
            Authentication authentication, // Нужен для получения роли
            @RequestParam(required = false) ApplicationStatus status) {

        Long userId = SecurityUtils.getCurrentUserId(authentication);
        Role userRole = authentication.getAuthorities().stream()
                // 1. Получаем первую же "роль", которая соответствует нашему enum
                .map(GrantedAuthority::getAuthority)
                // 2. Убираем префикс "ROLE_", если он есть (стандартная практика)
                .map(roleString -> roleString.startsWith("ROLE_") ? roleString.substring(5) : roleString)
                // 3. Преобразуем строку в наш enum Role
                .map(Role::valueOf)
                // 4. Находим первое совпадение
                .findFirst()
                // 5. Если по какой-то причине роль не найдена, бросаем исключение
                .orElseThrow(() -> new IllegalStateException("Не удалось определить роль пользователя."));

        return mentorshipService.getMyApplications(userId, userRole, status);
    }
}
