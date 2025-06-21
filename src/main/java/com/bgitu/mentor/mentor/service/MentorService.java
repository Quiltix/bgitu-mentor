package com.bgitu.mentor.mentor.service;


import com.bgitu.mentor.common.dto.PersonalInfoDto;
import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
import com.bgitu.mentor.common.service.FileStorageService;

import com.bgitu.mentor.mentor.dto.RegisterCardMentorDto;
import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.mentor.repository.MentorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentorService {


    private final MentorRepository mentorRepository;
    private final FileStorageService fileStorageService;
    private final PasswordEncoder passwordEncoder;


    public Mentor registerCardMentor(Authentication authentication, RegisterCardMentorDto cardDto, MultipartFile avatarFile){

        Mentor mentor = getMentorByAuth(authentication);


        mentor.setDescription(cardDto.getDescription());
        mentor.setSpeciality(cardDto.getSpeciality());
        mentor.setVkUrl(cardDto.getVkUrl());
        mentor.setTelegramUrl(cardDto.getTelegramUrl());

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarUrl = fileStorageService.storeAvatar(avatarFile, "mentor_" + mentor.getId());
            mentor.setAvatarUrl(avatarUrl);
        }

        return mentorRepository.save(mentor);
    }

    public Mentor getMentorByAuth(Authentication authentication){
        String email = authentication.getName();

        return mentorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Mentor not found"));

    }

    public Mentor updateMentorProfile(Authentication authentication, UpdatePersonalInfo dto) {

        Mentor mentor = getMentorByAuth(authentication);

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            mentor.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            mentor.setPassword(passwordEncoder.encode(dto.getPassword())); // обязательно хешируй!
        }

        if (dto.getFirstName() != null) {
            mentor.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null) {
            mentor.setLastName(dto.getLastName());
        }

        return mentorRepository.save(mentor);
    }

}
