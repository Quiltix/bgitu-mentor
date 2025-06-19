package com.bgitu.mentor.mentor.controller;

import com.bgitu.mentor.mentor.dto.RegisterCardDto;
import com.bgitu.mentor.mentor.service.MentorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("mentor")
public class MentorController {

    private final MentorService mentorService;


    @PreAuthorize("hasRole('MENTOR')")
    @PostMapping("/summary")
    public ResponseEntity<?> registerCardMentor(Authentication authentication, @RequestBody @Valid RegisterCardDto cardDto){

        return ResponseEntity.ok(mentorService.registerCardMentor(authentication,cardDto));
    }




}
