package com.bgitu.mentor.mentor.controller;

import com.bgitu.mentor.mentor.dto.RegisterCardMentorDto;
import com.bgitu.mentor.mentor.service.MentorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("mentor")
public class MentorController {

    private final MentorService mentorService;


    @PreAuthorize("hasRole('MENTOR')")
    @PostMapping(value ="/summary", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerCardMentor(Authentication authentication, @RequestPart("card") @Valid RegisterCardMentorDto cardDto, @RequestPart(value = "avatar", required = false) MultipartFile avatarFile){

        return ResponseEntity.ok(mentorService.registerCardMentor(authentication,cardDto,avatarFile));
    }




}
