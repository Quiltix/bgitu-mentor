package com.bgitu.mentor.student.service;


import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
import com.bgitu.mentor.common.service.FileStorageService;
import com.bgitu.mentor.mentor.dto.CardMentorDto;
import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.mentorship.model.Application;
import com.bgitu.mentor.mentorship.repository.ApplicationRepository;
import com.bgitu.mentor.student.dto.ApplicationStudentDto;
import com.bgitu.mentor.student.dto.RegisterStudentCardDto;
import com.bgitu.mentor.student.dto.UpdateStudentCardDto;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final FileStorageService fileStorageService;
    private final PasswordEncoder passwordEncoder;

    private final ApplicationRepository applicationRepository;


    public Student getStudentByAuth(Authentication authentication){
        Long longId = Long.parseLong(authentication.getName());

        return studentRepository.findById(longId)
                .orElseThrow(() -> new UsernameNotFoundException("Студент не найден"));

    }

    public Student updateStudentProfile(Authentication authentication, UpdatePersonalInfo dto) {

        Student student = getStudentByAuth(authentication);

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            student.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            student.setPassword(passwordEncoder.encode(dto.getPassword())); // обязательно хешируй!
        }

        return studentRepository.save(student);
    }

    public Student updateStudentCard(
            Authentication authentication,
            UpdateStudentCardDto dto,
            MultipartFile avatarFile
    ) {
        Student student = getStudentByAuth(authentication);

        if (dto.getDescription() != null) {
            student.setDescription(dto.getDescription());
        }
        if (dto.getVkUrl() != null) {
            student.setVkUrl(dto.getVkUrl());
        }
        if (dto.getTelegramUrl() != null) {
            student.setTelegramUrl(dto.getTelegramUrl());
        }
        if (dto.getFirstName() != null) {
            student.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            student.setLastName(dto.getLastName());
        }

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarUrl = fileStorageService.storeAvatar(avatarFile, "student_" + student.getId());
            student.setAvatarUrl(avatarUrl);
        }

        return studentRepository.save(student);
    }


    public CardMentorDto getMentorOfStudent(Authentication auth) {
        Student student = getStudentByAuth(auth);

        Mentor mentor = student.getMentor();
        if (mentor == null) {
            throw new IllegalStateException("У студента пока нет назначенного ментора");
        }

        return new CardMentorDto(mentor);
    }

    public List<ApplicationStudentDto> getStudentApplications(Authentication authentication) {
        String email = authentication.getName();
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        List<Application> applications = applicationRepository.findAllByStudent(student);

        return applications.stream()
                .map(ApplicationStudentDto::new)
                .toList();
    }



}
