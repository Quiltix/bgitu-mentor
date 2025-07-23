package com.bgitu.mentor.student.service;


import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
import com.bgitu.mentor.common.service.FileStorageService;
import com.bgitu.mentor.mentor.dto.CardMentorDto;
import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.mentorship.model.Application;
import com.bgitu.mentor.mentorship.repository.ApplicationRepository;
import com.bgitu.mentor.student.dto.ApplicationStudentDto;
import com.bgitu.mentor.student.dto.UpdateStudentCardDto;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.repository.StudentRepository;
import com.bgitu.mentor.user.service.AbstractBaseUserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class StudentServiceImpl extends AbstractBaseUserService<Student,StudentRepository> implements StudentService {


    private final ApplicationRepository applicationRepository;

    public StudentServiceImpl(StudentRepository studentRepository, PasswordEncoder passwordEncoder,
                              FileStorageService fileStorageService, ApplicationRepository applicationRepository) {
        super(studentRepository, passwordEncoder, fileStorageService, "Студент");
        this.applicationRepository = applicationRepository;
    }



    @Override
    public Student updateCard(Authentication authentication, UpdateStudentCardDto dto, MultipartFile avatarFile) {
        Student student = getByAuth(authentication);

        updateCardInternal(student, dto, avatarFile);
        return repository.save(student);
    }


    @Override
    public CardMentorDto getMentorOfStudent(Authentication auth) {
        Student student = getByAuth(auth);

        Mentor mentor = student.getMentor();
        if (mentor == null) {
            throw new IllegalStateException("У студента пока нет назначенного ментора");
        }

        return new CardMentorDto(mentor);
    }

    @Override
    public List<ApplicationStudentDto> getStudentApplications(Authentication authentication) {

        Student student = getByAuth(authentication);

        List<Application> applications = applicationRepository.findAllByStudent(student);

        return applications.stream()
                .map(ApplicationStudentDto::new)
                .toList();
    }



}
