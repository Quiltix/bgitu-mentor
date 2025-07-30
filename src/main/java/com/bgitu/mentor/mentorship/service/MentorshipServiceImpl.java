package com.bgitu.mentor.mentorship.service;


import com.bgitu.mentor.common.exception.ResourceNotFoundException;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.service.MentorService;
import com.bgitu.mentor.mentorship.dto.UpdateApplicationStatusDto;
import com.bgitu.mentor.mentorship.dto.ApplicationResponseDto;
import com.bgitu.mentor.mentorship.dto.MentorshipRequestDto;
import com.bgitu.mentor.mentorship.model.Application;
import com.bgitu.mentor.mentorship.model.ApplicationStatus;
import com.bgitu.mentor.mentorship.repository.ApplicationRepository;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorshipServiceImpl implements MentorshipService {


    private final ApplicationRepository applicationRepository;
    private final StudentService studentService;
    private final MentorService mentorService;

    @Override
    @Transactional
    public ApplicationResponseDto createApplication(Authentication authentication, MentorshipRequestDto dto) {
        Student student = studentService.getByAuth(authentication);

        Mentor mentor = mentorService.findById(dto.getMentorId());

        if (student.getMentor() != null) {
            throw new IllegalStateException("У вас уже есть ментор. Сначала прекратите текущее менторство.");
        }


        if (applicationRepository.existsByStudentAndMentorAndStatus(student, mentor, ApplicationStatus.PENDING)) {
            throw new IllegalStateException("Вы уже отправляли заявку этому ментору.");
        }
        Application app = new Application();
        app.setStudent(student);
        app.setMentor(mentor);
        app.setMessage(dto.getMessage());
        app.setStatus(ApplicationStatus.PENDING);

        return new ApplicationResponseDto(applicationRepository.save(app));
    }

    @Override
    @Transactional
    public void updateApplicationStatus(Authentication authentication, Long applicationId, UpdateApplicationStatusDto dto) {
        Mentor mentor = mentorService.getByAuth(authentication);
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Эта заявка не найдена"));

        // Критически важная проверка безопасности!
        if (!app.getMentor().getId().equals(mentor.getId())) {
            throw new SecurityException("Вы не можете отвечать на чужую заявку.");
        }

        if (app.getStatus() != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Нельзя изменить статус заявки, которая уже обработана.");
        }

        if (Boolean.TRUE.equals(dto.getAccepted())) {
            approveApplication(app);
        } else {
            app.setStatus(ApplicationStatus.REJECTED);
        }
        applicationRepository.save(app);
    }

    private void approveApplication(Application application) {
        application.setStatus(ApplicationStatus.ACCEPTED);

        Student student = application.getStudent();
        student.setMentor(application.getMentor());

        List<Application> otherPendingApps = applicationRepository
                .findAllByStudentAndStatus(student, ApplicationStatus.PENDING);

        otherPendingApps.forEach(otherApp -> otherApp.setStatus(ApplicationStatus.EXPIRED));
        applicationRepository.saveAll(otherPendingApps);
    }

    @Override
    public List<ApplicationResponseDto> getApplicationsForMentor(Authentication authentication, ApplicationStatus status) {
        Mentor mentor = mentorService.getByAuth(authentication);

        List<Application> applications = (status != null)
                ? applicationRepository.findAllByMentorAndStatus(mentor, status)
                : applicationRepository.findAllByMentor(mentor);

        return applications.stream().map(ApplicationResponseDto::new).toList();
    }
}
