package com.bgitu.mentor.mentorship.controller;

import com.bgitu.mentor.common.dto.MessageDto;
import com.bgitu.mentor.mentorship.dto.ApplicationDecisionDto;
import com.bgitu.mentor.mentorship.dto.ApplicationResponseDto;
import com.bgitu.mentor.mentorship.dto.MentorshipRequestDto;
import com.bgitu.mentor.mentorship.model.ApplicationStatus;
import com.bgitu.mentor.mentorship.service.MentorshipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/mentorship")
@RequiredArgsConstructor
@Tag(name = "Mentorship", description = "Управление заявками на менторство между студентами и менторами")
public class MentorshipController {

    private final MentorshipService mentorshipService;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/request")
    @Operation(summary = "Отправить заявку на менторство", description = "Позволяет студенту отправить заявку выбранному ментору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заявка успешно отправлена"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации или параметров запроса")
    })
    public ResponseEntity<MessageDto> requestMentorship(Authentication authentication, @RequestBody @Valid MentorshipRequestDto dto) {
        mentorshipService.requestMentorship(authentication,dto);
        return ResponseEntity.ok(new MessageDto("Заявка отправлена"));
    }

    @PreAuthorize("hasRole('MENTOR')")
    @PostMapping("/respond")
    @Operation(summary = "Ответить на заявку", description = "Позволяет ментору принять или отклонить заявку студента на менторство")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ответ на заявку принят"),
            @ApiResponse(responseCode = "404", description = "Заявка не найдена")
    })
    public ResponseEntity<MessageDto> respondToApplication(@RequestBody ApplicationDecisionDto dto) {
        mentorshipService.respondToApplication(dto);
        return ResponseEntity.ok( new MessageDto("Ответ принят"));
    }


    @PreAuthorize("hasRole('MENTOR')")
    @GetMapping("/applications/me")
    @Operation(summary = "Получить входящие заявки", description = "Возвращает список заявок, направленных этому ментору. Можно фильтровать по статусу.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список заявок успешно получен")
    })
    public ResponseEntity<List<ApplicationResponseDto>> getMentorApplications(
            Authentication authentication,
            @RequestParam(required = false) ApplicationStatus status
    ) {
        return ResponseEntity.ok(mentorshipService.getApplicationsForMentor(authentication, status));
    }


    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/student/reject")
    public ResponseEntity<MessageDto> studentRejectMentor(Authentication authentication) {
        mentorshipService.studentRejectMentorship(authentication);
        return ResponseEntity.ok(new MessageDto("Ментор успешно удален"));
    }

    @PreAuthorize("hasRole('MENTOR')")
    @PostMapping("/mentor/reject/{studentId}")
    public ResponseEntity<MessageDto> mentorRejectStudent( Authentication authentication, @PathVariable Long studentId) {
        mentorshipService.mentorRejectStudent(authentication, studentId);
        return ResponseEntity.ok(new MessageDto("Студент успешно удален"));
    }

}
