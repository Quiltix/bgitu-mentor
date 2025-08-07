package com.bgitu.mentor.student.controller;

import com.bgitu.mentor.common.SecurityUtils;
import com.bgitu.mentor.common.dto.UserCredentialsResponseDto;
import com.bgitu.mentor.common.dto.UserCredentialsUpdateRequestDto;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.student.data.dto.ApplicationOfStudentResponseDto;
import com.bgitu.mentor.student.data.dto.StudentDetailsResponseDto;
import com.bgitu.mentor.student.data.dto.StudentDetailsUpdateRequestDto;
import com.bgitu.mentor.student.service.StudentProfileService;
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


@RestController
@RequiredArgsConstructor
@Tag(name = "Student Profile", description = "Методы для взаимодействия с профилем студента")
@RequestMapping("/api/profiles/student")
public class StudentProfileController {


    private final StudentProfileService studentService;

    @Operation(summary = "Получение карточки студента", description = "Доступно только для роли STUDENT. Возвращает полную информацию по текущему студенту.")
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping()
    public ResponseEntity<StudentDetailsResponseDto> getCardStudent(Authentication authentication){
        Long studentId = SecurityUtils.getCurrentUserId(authentication);
        return ResponseEntity.ok(studentService.getPublicCardById(studentId));
    }


    @Operation(summary = "Обновление карточки студента", description = "Доступно только для роли STUDENT. Позволяет редактировать описание и загрузить новый аватар.")
    @PreAuthorize("hasRole('STUDENT')")
    @PatchMapping( consumes = "multipart/form-data")
    public ResponseEntity<StudentDetailsResponseDto> updateStudentCard(
            Authentication authentication,
            @RequestPart("card") StudentDetailsUpdateRequestDto dto,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile
    ) {
        Long studentId = SecurityUtils.getCurrentUserId(authentication);
        return ResponseEntity.ok(studentService.updateCard(studentId, dto, avatarFile));
    }

    @Operation(summary = "Получение моего профиля", description = "STUDENT. Возвращает профиль по авторизации")
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/settings")
    public ResponseEntity<UserCredentialsResponseDto> getMentorProfile(Authentication authentication) {
        Long studentId = SecurityUtils.getCurrentUserId(authentication);
        return ResponseEntity.ok(studentService.getPersonalInfo(studentId));
    }

    @Operation(summary = "Обновление профиля студента", description = "Доступно только для роли STUDENT. Позволяет редактировать имя, фамилию и email.")
    @PreAuthorize("hasRole('STUDENT')")
    @PatchMapping("/settings")
    public ResponseEntity<UserCredentialsResponseDto> updateMentorProfile(
            Authentication authentication,
            @RequestBody @Valid UserCredentialsUpdateRequestDto dto
    ) {
        Long studentId = SecurityUtils.getCurrentUserId(authentication);
        return ResponseEntity.ok(studentService.updateProfile(studentId, dto));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/mentor")
    @Operation(summary = "Получить информацию о менторе студента", description = "Доступно для роли STUDENT")
    public ResponseEntity<MentorDetailsResponseDto> getStudentMentor(Authentication authentication) {
        Long studentId = SecurityUtils.getCurrentUserId(authentication);
        return ResponseEntity.ok(studentService.getMentorOfStudent(studentId));
    }

    @Operation(summary = "Получение всех заявок студента", description = "Доступно только студенту")
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/applications")
    public List<ApplicationOfStudentResponseDto> getStudentApplications(Authentication authentication) {
        Long studentId = SecurityUtils.getCurrentUserId(authentication);
        return studentService.getStudentApplications(studentId);
    }

    @DeleteMapping("/mentor") // <-- DELETE на ресурс "ментор" внутри профиля
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Прекратить менторство", description = "Позволяет студенту отказаться от своего текущего ментора.")
    public ResponseEntity<Void> terminateMentorship(Authentication authentication) {
        Long studentId = SecurityUtils.getCurrentUserId(authentication);
        studentService.terminateCurrentMentorship(studentId);
        return ResponseEntity.noContent().build();
    }

}
