package com.bgitu.mentor.student.controller;

import com.bgitu.mentor.student.dto.RegisterStudentCardDto;
import com.bgitu.mentor.student.dto.StudentCardDto;
import com.bgitu.mentor.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping(value = "/summary", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StudentCardDto> registerStudentCard(
            Authentication authentication,
            @RequestPart("card") @Valid RegisterStudentCardDto cardDto,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile) {

        return ResponseEntity.ok(new StudentCardDto(studentService.registerStudentCard(authentication, cardDto, avatarFile)));
    }
}
