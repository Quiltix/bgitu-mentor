package com.bgitu.mentor.student.controller;

import com.bgitu.mentor.common.dto.PersonalInfoDto;
import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
import com.bgitu.mentor.mentor.data.dto.CardMentorDto;
import com.bgitu.mentor.student.dto.ApplicationStudentDto;
import com.bgitu.mentor.student.dto.StudentCardDto;
import com.bgitu.mentor.student.dto.UpdateStudentCardDto;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.service.StudentService;
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


    private final StudentService studentService;

    @Operation(summary = "Получение карточки студента", description = "Доступно только для роли STUDENT. Возвращает полную информацию по текущему студенту.")
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/summary")
    public ResponseEntity<StudentCardDto> getCardStudent(Authentication authentication){
        return ResponseEntity.ok(new StudentCardDto(studentService.getByAuth(authentication)));
    }


    @Operation(summary = "Обновление карточки студента", description = "Доступно только для роли STUDENT. Позволяет редактировать описание и загрузить новый аватар.")
    @PreAuthorize("hasRole('STUDENT')")
    @PatchMapping(value = "/summary", consumes = "multipart/form-data")
    public ResponseEntity<StudentCardDto> updateStudentCard(
            Authentication authentication,
            @RequestPart("card") UpdateStudentCardDto dto,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile
    ) {
        Student updated = studentService.updateCard(authentication, dto, avatarFile);
        return ResponseEntity.ok(new StudentCardDto(updated));
    }

    @Operation(summary = "Получение моего профиля", description = "STUDENT. Возвращает профиль по авторизации")
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/profile")
    public ResponseEntity<PersonalInfoDto> getMentorProfile(Authentication authentication) {
        return ResponseEntity.ok(new PersonalInfoDto(studentService.getByAuth(authentication)));
    }

    @Operation(summary = "Обновление профиля студента", description = "Доступно только для роли STUDENT. Позволяет редактировать имя, фамилию и email.")
    @PreAuthorize("hasRole('STUDENT')")
    @PatchMapping("/profile")
    public ResponseEntity<PersonalInfoDto> updateMentorProfile(
            Authentication authentication,
            @RequestBody @Valid UpdatePersonalInfo dto
    ) {
        Student updated = studentService.updateProfile(authentication, dto);
        return ResponseEntity.ok(new PersonalInfoDto(updated));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/mentor")
    @Operation(summary = "Получить информацию о менторе студента", description = "Доступно для роли STUDENT")
    public ResponseEntity<CardMentorDto> getStudentMentor(Authentication authentication) {
        return ResponseEntity.ok(studentService.getMentorOfStudent(authentication));
    }

    @Operation(summary = "Получение всех заявок студента", description = "Доступно только студенту")
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/applications")
    public List<ApplicationStudentDto> getStudentApplications(Authentication authentication) {
        return studentService.getStudentApplications(authentication);
    }

    @DeleteMapping("/mentor") // <-- DELETE на ресурс "ментор" внутри профиля
    @PreAuthorize("hasRole('STUDENT')")
    @Operation(summary = "Прекратить менторство", description = "Позволяет студенту отказаться от своего текущего ментора.")
    public ResponseEntity<Void> terminateMentorship(Authentication authentication) {
        studentService.terminateCurrentMentorship(authentication);
        return ResponseEntity.noContent().build();
    }

}
