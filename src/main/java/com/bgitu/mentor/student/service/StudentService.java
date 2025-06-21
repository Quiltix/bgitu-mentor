package com.bgitu.mentor.student.service;


import com.bgitu.mentor.common.service.FileStorageService;
import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.student.dto.RegisterStudentCardDto;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final FileStorageService fileStorageService;

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
}
