package com.bgitu.mentor.mentorship.service;


import com.bgitu.mentor.common.exception.ResourceNotFoundException;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentorship.data.dto.ApplicationDecisionRequestDto;
import com.bgitu.mentor.mentorship.data.dto.ApplicationDetailsResponseDto;
import com.bgitu.mentor.mentorship.data.dto.ApplicationCreateRequestDto;
import com.bgitu.mentor.mentorship.data.model.Application;
import com.bgitu.mentor.mentorship.data.model.ApplicationStatus;
import com.bgitu.mentor.mentorship.data.repository.ApplicationRepository;
import com.bgitu.mentor.student.data.model.Student;
import com.bgitu.mentor.user.service.UserFinder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorshipServiceImpl implements MentorshipService {


    private final ApplicationRepository applicationRepository;
    private final UserFinder userFinder;
    private final MentorshipLifecycleService mentorshipLifecycleService;

    @Override
    @Transactional
    public ApplicationDetailsResponseDto createApplication(Long studentId, ApplicationCreateRequestDto dto) {
        Student student = userFinder.findStudentById(studentId);

        Mentor mentor = userFinder.findMentorById(dto.getMentorId());

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

        return new ApplicationDetailsResponseDto(applicationRepository.save(app));
    }

    @Override
    @Transactional
    public void updateApplicationStatus(Long mentorId, Long applicationId, ApplicationDecisionRequestDto dto) {
        Mentor mentor = userFinder.findMentorById(mentorId);
        Application app = findAndVerifyApplicationOwner(applicationId, mentor);



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

        mentorshipLifecycleService.establishLink(application.getMentor(), application.getStudent());

        List<Application> otherPendingApps = applicationRepository
                .findAllByStudentAndStatus(application.getStudent(), ApplicationStatus.PENDING);

        otherPendingApps.forEach(otherApp -> otherApp.setStatus(ApplicationStatus.EXPIRED));

        // Сохраняем все измененные заявки (включая текущую)
        applicationRepository.save(application);

        if (!otherPendingApps.isEmpty()) {
            applicationRepository.saveAll(otherPendingApps);
        }
    }

    @Override
    public List<ApplicationDetailsResponseDto> getApplicationsForMentor(Long mentorId, ApplicationStatus status) {
        Mentor mentor = userFinder.findMentorById(mentorId);

        List<Application> applications = (status != null)
                ? applicationRepository.findAllByMentorAndStatus(mentor, status)
                : applicationRepository.findAllByMentor(mentor);

        return applications.stream().map(ApplicationDetailsResponseDto::new).toList();
    }

    private Application findAndVerifyApplicationOwner(Long applicationId, Mentor mentor) {
        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Заявка с id=" + applicationId + " не найдена"));

        if (!app.getMentor().getId().equals(mentor.getId())) {
            throw new SecurityException("Вы не можете отвечать на чужую заявку.");
        }
        return app;
    }
}
