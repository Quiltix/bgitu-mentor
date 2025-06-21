package com.bgitu.mentor.student.controller;


import com.bgitu.mentor.common.dto.MessageDto;
import com.bgitu.mentor.common.dto.PersonalInfoDto;
import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
import com.bgitu.mentor.mentor.dto.CardMentorDto;
import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.student.dto.RegisterStudentCardDto;
import com.bgitu.mentor.student.dto.StudentCardDto;
import com.bgitu.mentor.student.dto.UpdateStudentCardDto;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping(value = "/summary", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StudentCardDto> registerStudentCard(
            Authentication authentication,
            @RequestPart("card") @Valid RegisterStudentCardDto cardDto,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile
    ) {

        return ResponseEntity.ok(new StudentCardDto(studentService.registerStudentCard(authentication, cardDto, avatarFile)));
    }


    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/summary")
    public ResponseEntity<StudentCardDto> getCardStudent(Authentication authentication){
        return ResponseEntity.ok(new StudentCardDto(studentService.getStudentByAuth(authentication)));
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PatchMapping("/profile")
    public ResponseEntity<PersonalInfoDto> updateMentorProfile(
            Authentication authentication,
            @RequestBody @Valid UpdatePersonalInfo dto
    ) {
        Student updated = studentService.updateStudentProfile(authentication, dto);
        return ResponseEntity.ok(new PersonalInfoDto(updated));

    }

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


}
