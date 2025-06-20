package com.bgitu.mentor.student.service;


import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
import com.bgitu.mentor.common.service.FileStorageService;
import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.student.dto.RegisterStudentCardDto;
import com.bgitu.mentor.student.dto.UpdateStudentCardDto;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final FileStorageService fileStorageService;
    private final PasswordEncoder passwordEncoder;


    public Student registerStudentCard(Authentication authentication, RegisterStudentCardDto dto, MultipartFile avatarFile) {
        String email = authentication.getName();

        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Student not found"));

        student.setDescription(dto.getDescription());
        student.setVkUrl(dto.getVkUrl());
        student.setTelegramUrl(dto.getTelegramUrl());

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarUrl = fileStorageService.storeAvatar(avatarFile, "student_" + student.getId());
            student.setAvatarUrl(avatarUrl);
        }

        return studentRepository.save(student);
    }

    public Student getStudentByAuth(Authentication authentication){
        String email = authentication.getName();

        return studentRepository.findByEmail(email)
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

        if (dto.getFirstName() != null) {
            student.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null) {
            student.setLastName(dto.getLastName());
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

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarUrl = fileStorageService.storeAvatar(avatarFile, "student_" + student.getId());
            student.setAvatarUrl(avatarUrl);
        }

        return studentRepository.save(student);
    }

}
