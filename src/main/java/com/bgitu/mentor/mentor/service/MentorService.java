package com.bgitu.mentor.mentor.service;


import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
import com.bgitu.mentor.common.service.FileStorageService;

import com.bgitu.mentor.mentor.dto.RegisterCardMentorDto;
import com.bgitu.mentor.mentor.dto.UpdateMentorCardDto;
import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.mentor.model.Speciality;
import com.bgitu.mentor.mentor.repository.MentorRepository;
import com.bgitu.mentor.mentor.repository.SpecialityRepository;
import com.bgitu.mentor.student.dto.UpdateStudentCardDto;
import com.bgitu.mentor.student.model.Student;
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
    private final SpecialityRepository specialityRepository;


    public Mentor registerCardMentor(Authentication authentication, RegisterCardMentorDto cardDto, MultipartFile avatarFile){

        Mentor mentor = getMentorByAuth(authentication);


        mentor.setDescription(cardDto.getDescription());
        Speciality speciality = specialityRepository.findById(cardDto.getSpecialityId())
                .orElseThrow(() -> new IllegalArgumentException("Специальность не найдена"));
        mentor.setSpeciality(speciality);
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
    public Mentor updateMentorCard(Authentication authentication, UpdateMentorCardDto dto, MultipartFile avatarFile) {


        Mentor mentor = getMentorByAuth(authentication);

        if (dto.getDescription() != null) {
            mentor.setDescription(dto.getDescription());
        }

        if (dto.getVkUrl() != null) {
            mentor.setVkUrl(dto.getVkUrl());
        }

        if (dto.getTelegramUrl() != null) {
            mentor.setTelegramUrl(dto.getTelegramUrl());
        }

        if (dto.getSpecialityId() != null) {
            Speciality speciality = specialityRepository.findById(dto.getSpecialityId())
                    .orElseThrow(() -> new IllegalArgumentException("Специальность не найдена"));
            mentor.setSpeciality(speciality);
        }

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarUrl = fileStorageService.storeAvatar(avatarFile, "mentor_" + mentor.getId());
            mentor.setAvatarUrl(avatarUrl);
        }

        return mentorRepository.save(mentor);
    }


}
