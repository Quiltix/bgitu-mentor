package com.bgitu.mentor.mentorship.service;

import com.bgitu.mentor.auth.Role;
import com.bgitu.mentor.exception.dto.ResourceNotFoundException;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentorship.data.ApplicationMapper;
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

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorshipServiceImpl implements MentorshipService {

  private final ApplicationRepository applicationRepository;
  private final UserFinder userFinder;
  private final MentorshipLifecycleService mentorshipLifecycleService;
  private final ApplicationMapper applicationMapper;

  @Override
  @Transactional
  public ApplicationDetailsResponseDto createApplication(
      Long studentId, ApplicationCreateRequestDto dto) {
    Student student = userFinder.findStudentById(studentId);

    Mentor mentor = userFinder.findMentorById(dto.getMentorId());

    if (student.getMentor() != null) {
      throw new IllegalStateException(
          "У вас уже есть ментор. Сначала прекратите текущее менторство.");
    }

    if (applicationRepository.existsByStudentIdAndMentorIdAndStatus(
        studentId, mentor.getId(), ApplicationStatus.PENDING)) {
      throw new IllegalStateException("Вы уже отправляли заявку этому ментору.");
    }
    Application app = new Application();
    app.setStudent(student);
    app.setMentor(mentor);
    app.setMessage(dto.getMessage());
    app.setStatus(ApplicationStatus.PENDING);

    return applicationMapper.toDetailsDto(applicationRepository.save(app));
  }

  @Override
  @Transactional
  public void updateApplicationStatus(
      Long mentorId, Long applicationId, ApplicationDecisionRequestDto dto) {
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

    mentorshipLifecycleService.establishLink(
        application.getMentor().getId(), application.getStudent().getId());

    List<Application> otherPendingApps =
        applicationRepository.findAllByStudentIdAndStatus(
            application.getStudent().getId(), ApplicationStatus.PENDING);

    otherPendingApps.forEach(otherApp -> otherApp.setStatus(ApplicationStatus.EXPIRED));

    if (!otherPendingApps.isEmpty()) {
      applicationRepository.saveAll(otherPendingApps);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<?> getMyApplications(Long userId, Role role, ApplicationStatus status) {
    if (role == Role.MENTOR) {
      List<Application> applications =
          (status != null)
              ? applicationRepository.findAllByMentorIdAndStatus(userId, status)
              : applicationRepository.findAllByMentorId(userId);
      return applicationMapper.toDetailsDtoList(applications);
    }

    if (role == Role.STUDENT) {
      List<Application> applications =
          (status != null)
              ? applicationRepository.findAllByStudentIdAndStatus(userId, status)
              : applicationRepository.findAllByStudentId(userId);
      return applicationMapper.toStudentApplicationDtoList(applications);
    }

    return Collections.emptyList();
  }

  private Application findAndVerifyApplicationOwner(Long applicationId, Mentor mentor) {
    Application app =
        applicationRepository
            .findById(applicationId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException("Заявка с id=" + applicationId + " не найдена"));

    if (!app.getMentor().getId().equals(mentor.getId())) {
      throw new SecurityException("Вы не можете отвечать на чужую заявку.");
    }
    return app;
  }
}
