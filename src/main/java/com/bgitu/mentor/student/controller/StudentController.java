package com.bgitu.mentor.student.controller;


import com.bgitu.mentor.common.dto.MessageDto;
import com.bgitu.mentor.common.dto.PersonalInfoDto;
import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
import com.bgitu.mentor.mentor.dto.CardMentorDto;
import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.student.dto.RegisterStudentCardDto;
import com.bgitu.mentor.student.dto.StudentCardDto;
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
    public ResponseEntity<?> updateMentorProfile(
            Authentication authentication,
            @RequestBody @Valid UpdatePersonalInfo dto
    ) {
        try {
            Student updated = studentService.updateStudentProfile(authentication, dto);
            return ResponseEntity.ok(new PersonalInfoDto(updated));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body(new MessageDto("Ошибка обновления профиля"));
        }
    }

}
