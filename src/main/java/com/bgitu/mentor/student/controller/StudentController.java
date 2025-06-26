package com.bgitu.mentor.student.controller;


import com.bgitu.mentor.common.dto.MessageDto;
import com.bgitu.mentor.common.dto.PersonalInfoDto;
import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
import com.bgitu.mentor.mentor.dto.CardMentorDto;
import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.student.dto.ApplicationStudentDto;
import com.bgitu.mentor.student.dto.RegisterStudentCardDto;
import com.bgitu.mentor.student.dto.StudentCardDto;
import com.bgitu.mentor.student.dto.UpdateStudentCardDto;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Student", description = "Методы для взаимодействия с карточками и профилем студента")
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;



    @Operation(summary = "Получение карточки студента", description = "Доступно только для роли STUDENT. Возвращает полную информацию по текущему студенту.")
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/summary")
    public ResponseEntity<StudentCardDto> getCardStudent(Authentication authentication){
        return ResponseEntity.ok(new StudentCardDto(studentService.getStudentByAuth(authentication)));
    }


    @Operation(summary = "Обновление профиля студента", description = "Доступно только для роли STUDENT. Позволяет редактировать имя, фамилию и email.")
    @PreAuthorize("hasRole('STUDENT')")
    @PatchMapping("/profile")
    public ResponseEntity<PersonalInfoDto> updateMentorProfile(
            Authentication authentication,
            @RequestBody @Valid UpdatePersonalInfo dto
    ) {
        Student updated = studentService.updateStudentProfile(authentication, dto);
        return ResponseEntity.ok(new PersonalInfoDto(updated));
    }


    @Operation(summary = "Обновление карточки студента", description = "Доступно только для роли STUDENT. Позволяет редактировать описание и загрузить новый аватар.")
    @PreAuthorize("hasRole('STUDENT')")
    @PatchMapping(value = "/summary", consumes = "multipart/form-data")
    public ResponseEntity<StudentCardDto> updateStudentCard(
            Authentication authentication,
            @RequestPart("card") UpdateStudentCardDto dto,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile
    ) {
        Student updated = studentService.updateStudentCard(authentication, dto, avatarFile);
        return ResponseEntity.ok(new StudentCardDto(updated));
    }


    @Operation(summary = "Получение моего профиля", description = "STUDENT. Возвращает профиль по авторизации")
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/profile")
    public ResponseEntity<PersonalInfoDto> getMentorProfile(Authentication authentication) {
        return ResponseEntity.ok(new PersonalInfoDto(studentService.getStudentByAuth(authentication)));
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

}
