package com.bgitu.mentor.mentor.controller;

import com.bgitu.mentor.common.dto.MessageDto;
import com.bgitu.mentor.common.dto.PersonalInfoDto;
import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
import com.bgitu.mentor.mentor.dto.CardMentorDto;
import com.bgitu.mentor.mentor.dto.MentorShortDto;
import com.bgitu.mentor.mentor.dto.RegisterCardMentorDto;
import com.bgitu.mentor.mentor.dto.UpdateMentorCardDto;
import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.mentor.service.MentorService;
import com.bgitu.mentor.student.dto.StudentCardDto;
import com.bgitu.mentor.student.dto.UpdateStudentCardDto;
import com.bgitu.mentor.student.model.Student;
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
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mentor")
public class MentorController {

    private final MentorService mentorService;


    @PreAuthorize("hasRole('MENTOR')")
    @PostMapping(value = "/summary", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CardMentorDto> registerCardMentor(
            Authentication authentication,
            @RequestPart("card") @Valid RegisterCardMentorDto cardDto,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile
    ) {

        return ResponseEntity.ok(new CardMentorDto(mentorService.registerCardMentor(authentication, cardDto, avatarFile)));
    }

    @PreAuthorize("hasRole('MENTOR')")
    @GetMapping("/summary")
    public ResponseEntity<CardMentorDto> getCardMentor(Authentication authentication) {
        return ResponseEntity.ok(new CardMentorDto(mentorService.getMentorByAuth(authentication)));
    }

    @PreAuthorize("hasRole('MENTOR')")
    @PatchMapping("/profile")
    public ResponseEntity<PersonalInfoDto> updateMentorProfile(
            Authentication authentication,
            @RequestBody @Valid UpdatePersonalInfo dto
    ) {

        Mentor updated = mentorService.updateMentorProfile(authentication, dto);
        return ResponseEntity.ok(new PersonalInfoDto(updated));

    }

    @PreAuthorize("hasRole('MENTOR')")
    @PatchMapping(value = "/summary", consumes = "multipart/form-data")
    public ResponseEntity<CardMentorDto> updateMentorCard(
            Authentication authentication,
            @RequestPart("card") UpdateMentorCardDto dto,
            @RequestPart(value = "avatar", required = false) MultipartFile avatarFile
    ) {

        Mentor updated = mentorService.updateMentorCard(authentication, dto, avatarFile);
        return ResponseEntity.ok(new CardMentorDto(updated));
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    @GetMapping("/popular")
    public ResponseEntity<List<CardMentorDto>> getTopMentors() {
        return ResponseEntity.ok(mentorService.getTopMentors());
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    @GetMapping("/all")
    public List<MentorShortDto> getAllMentorsShort(@RequestParam(required = false) Long specialityId) {
        return mentorService.getAllShort(Optional.ofNullable(specialityId));
    }

    @PreAuthorize("hasRole('STUDENT') or hasRole('MENTOR')")
    @GetMapping("/{id}")
    public CardMentorDto getMentorDetails(@PathVariable Long id) {
        return mentorService.getById(id);
    }
}








