package com.bgitu.mentor.mentor.service;

import com.bgitu.mentor.auth.security.JwtTokenProvider;
import com.bgitu.mentor.mentor.dto.RegisterCardDto;
import com.bgitu.mentor.mentor.model.Mentor;

public class MentorService {


    registerCardMentor

    public void registerCard(RegisterCardDto dto) {
        String email = JwtTokenProvider.getCurrentUserEmail();

        Mentor mentor = mentorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Mentor not found"));

        mentor.setDescription(dto.getDescription());
        mentor.setAvatarUrl(dto.getAvatarUrl());
        mentor.setSpecialty(dto.getSpecialty());
        mentor.setVkUrl(dto.getVkUrl());
        mentor.setTelegramUrl(dto.getTelegramUrl());

        mentorRepository.save(mentor);
    }
}
