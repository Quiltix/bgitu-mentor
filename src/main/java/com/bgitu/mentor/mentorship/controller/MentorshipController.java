package com.bgitu.mentor.mentorship.controller;


import com.bgitu.mentor.common.SecurityUtils;
import com.bgitu.mentor.mentorship.dto.UpdateApplicationStatusDto;
import com.bgitu.mentor.mentorship.dto.ApplicationResponseDto;
import com.bgitu.mentor.mentorship.dto.MentorshipRequestDto;
import com.bgitu.mentor.mentorship.model.ApplicationStatus;
import com.bgitu.mentor.mentorship.service.MentorshipService;
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
@RequestMapping("/api/mentorship/applications")
@RequiredArgsConstructor
@Tag(name = "Mentorships", description = "Управление заявками на менторство между студентами и менторами")
public class MentorshipController {

    private final MentorshipService mentorshipService;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping()
    @Operation(summary = "Отправить заявку на менторство", description = "Позволяет студенту отправить заявку выбранному ментору")
    public ResponseEntity<ApplicationResponseDto> requestMentorship(Authentication authentication, @RequestBody @Valid MentorshipRequestDto dto) {
        Long studentId = SecurityUtils.getCurrentUserId(authentication);
        ApplicationResponseDto responseDto =  mentorshipService.createApplication(studentId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PreAuthorize("hasRole('MENTOR')")
    @PatchMapping("/{applicationId}")
    @Operation(summary = "Ответить на заявку", description = "Ментор обновляет статус существующей заявки (Принять/Отклонить)")
    public ResponseEntity<Void> respondToApplication(
            Authentication authentication,
            @PathVariable Long applicationId,
            @RequestBody @Valid UpdateApplicationStatusDto dto) {
        Long mentorId = SecurityUtils.getCurrentUserId(authentication);
        mentorshipService.updateApplicationStatus(mentorId, applicationId, dto);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("hasRole('MENTOR')")
    @GetMapping
    @Operation(summary = "Получить входящие заявки ментора", description = "Возвращает список заявок, направленных текущему ментору")
    public ResponseEntity<List<ApplicationResponseDto>> getMentorApplications(
            Authentication authentication,
            @RequestParam(required = false) ApplicationStatus status) {
        Long mentorId = SecurityUtils.getCurrentUserId(authentication);
        return ResponseEntity.ok(mentorshipService.getApplicationsForMentor(mentorId, status));
    }
}
