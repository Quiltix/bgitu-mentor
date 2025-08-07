package com.bgitu.mentor.mentorship.controller;


import com.bgitu.mentor.common.SecurityUtils;
import com.bgitu.mentor.mentorship.data.dto.ApplicationDecisionRequestDto;
import com.bgitu.mentor.mentorship.data.dto.ApplicationDetailsResponseDto;
import com.bgitu.mentor.mentorship.data.dto.ApplicationCreateRequestDto;
import com.bgitu.mentor.mentorship.data.model.ApplicationStatus;
import com.bgitu.mentor.mentorship.service.MentorshipService;
import com.bgitu.mentor.student.data.dto.ApplicationOfStudentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    @Operation(summary = "Отправить заявку на менторство", description = "Позволяет студенту отправить заявку выбранному ментору")
    public ResponseEntity<ApplicationDetailsResponseDto> requestMentorship(Authentication authentication, @RequestBody @Valid ApplicationCreateRequestDto dto) {
        Long studentId = SecurityUtils.getCurrentUserId(authentication);
        ApplicationDetailsResponseDto responseDto =  mentorshipService.createApplication(studentId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PreAuthorize("hasRole('MENTOR')")
    @PatchMapping("/{applicationId}")
    @Operation(summary = "Ответить на заявку", description = "Ментор обновляет статус существующей заявки (Принять/Отклонить)")
    public ResponseEntity<Void> respondToApplication(
            Authentication authentication,
            @PathVariable Long applicationId,
            @RequestBody @Valid ApplicationDecisionRequestDto dto) {
        Long mentorId = SecurityUtils.getCurrentUserId(authentication);
        mentorshipService.updateApplicationStatus(mentorId, applicationId, dto);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasRole('MENTOR')")
    @GetMapping("/mentor")
    @Operation(summary = "Получить входящие заявки ментора", description = "Возвращает список заявок, направленных текущему ментору")
    public ResponseEntity<List<ApplicationDetailsResponseDto>> getMentorApplications(
            Authentication authentication,
            @RequestParam(required = false) ApplicationStatus status) {
        Long mentorId = SecurityUtils.getCurrentUserId(authentication);
        return ResponseEntity.ok(mentorshipService.getApplicationsForMentor(mentorId, status));
    }

    @Operation(summary = "Получение всех заявок студента", description = "Доступно только студенту")
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student")
    public List<ApplicationOfStudentResponseDto> getStudentApplications(Authentication authentication, @RequestParam(required = false) ApplicationStatus status) {
        Long studentId = SecurityUtils.getCurrentUserId(authentication);
        return mentorshipService.getApplicationsForStudent(studentId, status);
    }
}
