package com.bgitu.mentor.student.controller;

import com.bgitu.mentor.common.SecurityUtils;
import com.bgitu.mentor.common.dto.UserCredentialsResponseDto;
import com.bgitu.mentor.common.dto.UserCredentialsUpdateRequestDto;
import com.bgitu.mentor.mentor.data.dto.MentorDetailsResponseDto;
import com.bgitu.mentor.student.data.dto.StudentDetailsResponseDto;
import com.bgitu.mentor.student.data.dto.StudentDetailsUpdateRequestDto;
import com.bgitu.mentor.student.service.StudentProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequiredArgsConstructor
@Tag(name = "Student Profile", description = "Методы для взаимодействия с профилем студента")
@RequestMapping("/api/profiles/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentProfileController {


    private final StudentProfileService studentService;

    @Operation(summary = "Получить свою карточку", description = "Доступно только для роли STUDENT. Возвращает полную информацию по текущему студенту.")
    @GetMapping()
    public StudentDetailsResponseDto getCardStudent(Authentication authentication){
        Long studentId = SecurityUtils.getCurrentUserId(authentication);
        return studentService.getPublicCardById(studentId);
    }


    @Operation(summary = "Обновить свою карточку", description = "Доступно только для роли STUDENT. Позволяет редактировать описание и загрузить новый аватар.")
    @PatchMapping( consumes = "multipart/form-data")
    public StudentDetailsResponseDto updateStudentCard(
            Authentication authentication,
            @RequestPart("card") StudentDetailsUpdateRequestDto dto,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile
    ) {
        Long studentId = SecurityUtils.getCurrentUserId(authentication);
        return studentService.updateCard(studentId, dto, avatarFile);
    }

    @Operation(summary = "Получение своего профиля", description = "STUDENT. Возвращает профиль по авторизации")
    @GetMapping("/credentials")
    public UserCredentialsResponseDto getMentorProfile(Authentication authentication) {
        Long studentId = SecurityUtils.getCurrentUserId(authentication);
        return studentService.getPersonalInfo(studentId);
    }

    @Operation(summary = "Обновление профиля студента", description = "Доступно только для роли STUDENT. Позволяет редактировать имя, фамилию и email.")
    @PatchMapping("/credentials")
    public UserCredentialsResponseDto updateMentorProfile(
            Authentication authentication,
            @RequestBody @Valid UserCredentialsUpdateRequestDto dto
    ) {
        Long studentId = SecurityUtils.getCurrentUserId(authentication);
        return studentService.updateProfile(studentId, dto);
    }

    @GetMapping("/mentor")
    @Operation(summary = "Получить информацию о менторе студента", description = "Доступно для роли STUDENT")
    public MentorDetailsResponseDto getStudentMentor(Authentication authentication) {
        Long studentId = SecurityUtils.getCurrentUserId(authentication);
        return studentService.getMentorOfStudent(studentId);
    }

    @DeleteMapping("/mentor")
    @Operation(summary = "Прекратить менторство", description = "Позволяет студенту отказаться от своего текущего ментора.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void terminateMentorship(Authentication authentication) {
        Long studentId = SecurityUtils.getCurrentUserId(authentication);
        studentService.terminateCurrentMentorship(studentId);
    }

}
