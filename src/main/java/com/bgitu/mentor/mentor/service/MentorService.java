package com.bgitu.mentor.mentor.service;


import com.bgitu.mentor.mentor.dto.CardMentorDto;
import com.bgitu.mentor.mentor.dto.RegisterCardDto;
import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.mentor.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentorService {


    private final MentorRepository mentorRepository;


    public CardMentorDto registerCardMentor(Authentication authentication, RegisterCardDto cardDto){
        String email = authentication.getName();

        Mentor mentor = mentorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Mentor not found"));

        mentor.setDescription(cardDto.getDescription());
        mentor.setAvatarUrl(cardDto.getAvatarUrl());
        mentor.setSpecialty(cardDto.getSpecialty());
        mentor.setVkUrl(cardDto.getVkUrl());
        mentor.setTelegramUrl(cardDto.getTelegramUrl());

        return  new CardMentorDto(mentorRepository.save(mentor));
    }
}
