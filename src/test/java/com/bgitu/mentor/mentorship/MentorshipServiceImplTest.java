package com.bgitu.mentor.mentorship;

import com.bgitu.mentor.common.exception.ResourceNotFoundException;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentorship.data.ApplicationMapper;
import com.bgitu.mentor.mentorship.data.dto.ApplicationCreateRequestDto;
import com.bgitu.mentor.mentorship.data.dto.ApplicationDecisionRequestDto;
import com.bgitu.mentor.mentorship.data.dto.ApplicationDetailsResponseDto;
import com.bgitu.mentor.mentorship.data.model.Application;
import com.bgitu.mentor.mentorship.data.model.ApplicationStatus;
import com.bgitu.mentor.mentorship.data.repository.ApplicationRepository;
import com.bgitu.mentor.mentorship.service.MentorshipLifecycleService;
import com.bgitu.mentor.mentorship.service.MentorshipServiceImpl;
import com.bgitu.mentor.student.data.model.Student;
import com.bgitu.mentor.user.service.UserFinder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MentorshipServiceImplTest {

  @Mock private ApplicationRepository applicationRepository;
  @Mock private UserFinder userFinder;
  @Mock private MentorshipLifecycleService mentorshipLifecycleService;
  @Mock private ApplicationMapper applicationMapper;

  @InjectMocks private MentorshipServiceImpl mentorshipServiceImpl;

  @DisplayName(
      "createApplication | Should create application when student has no mentor and no pending application")
  @Test
  void createApplication_createsApplication_whenStudentHasNoMentorAndNoPendingApplication() {
    Long studentId = 1L;
    Long mentorId = 2L;
    ApplicationCreateRequestDto dto = new ApplicationCreateRequestDto();
    dto.setMentorId(mentorId);
    dto.setMessage("Test message");

    Student student = new Student();
    student.setId(studentId);
    student.setMentor(null);

    Mentor mentor = new Mentor();
    mentor.setId(mentorId);

    Application application = new Application();
    application.setId(3L);

    when(userFinder.findStudentById(studentId)).thenReturn(student);
    when(userFinder.findMentorById(mentorId)).thenReturn(mentor);
    when(applicationRepository.existsByStudentIdAndMentorIdAndStatus(
            studentId, mentorId, ApplicationStatus.PENDING))
        .thenReturn(false);
    when(applicationRepository.save(any(Application.class))).thenReturn(application);
    when(applicationMapper.toDetailsDto(application))
        .thenReturn(new ApplicationDetailsResponseDto());

    ApplicationDetailsResponseDto result = mentorshipServiceImpl.createApplication(studentId, dto);

    verify(userFinder, times(1)).findStudentById(studentId);
    verify(userFinder, times(1)).findMentorById(mentorId);
    verify(applicationRepository, times(1))
        .existsByStudentIdAndMentorIdAndStatus(studentId, mentorId, ApplicationStatus.PENDING);
    verify(applicationRepository, times(1)).save(any(Application.class));
    assertNotNull(result);
  }

  @DisplayName("createApplication | Should throw exception when student already has a mentor")
  @Test
  void createApplication_throwsException_whenStudentAlreadyHasMentor() {
    Long studentId = 1L;
    Long mentorId = 2L;
    ApplicationCreateRequestDto dto = new ApplicationCreateRequestDto();
    dto.setMentorId(mentorId);

    Student student = new Student();
    student.setId(studentId);
    student.setMentor(new Mentor());

    when(userFinder.findStudentById(studentId)).thenReturn(student);

    assertThrows(
        IllegalStateException.class, () -> mentorshipServiceImpl.createApplication(studentId, dto));

    verify(userFinder, times(1)).findStudentById(studentId);
    verifyNoInteractions(applicationRepository);
  }

  @DisplayName("createApplication | Should throw exception when pending application already exists")
  @Test
  void createApplication_throwsException_whenPendingApplicationExists() {
    Long studentId = 1L;
    Long mentorId = 2L;
    ApplicationCreateRequestDto dto = new ApplicationCreateRequestDto();
    dto.setMentorId(mentorId);

    Student student = new Student();
    student.setId(studentId);
    student.setMentor(null);

    Mentor mentor = new Mentor();
    mentor.setId(mentorId);

    when(userFinder.findStudentById(studentId)).thenReturn(student);
    when(userFinder.findMentorById(mentorId)).thenReturn(mentor);
    when(applicationRepository.existsByStudentIdAndMentorIdAndStatus(
            studentId, mentorId, ApplicationStatus.PENDING))
        .thenReturn(true);

    assertThrows(
        IllegalStateException.class, () -> mentorshipServiceImpl.createApplication(studentId, dto));

    verify(userFinder, times(1)).findStudentById(studentId);
    verify(userFinder, times(1)).findMentorById(mentorId);
    verify(applicationRepository, times(1))
        .existsByStudentIdAndMentorIdAndStatus(studentId, mentorId, ApplicationStatus.PENDING);
    verifyNoMoreInteractions(applicationRepository);
  }

  @DisplayName(
      "updateApplicationStatus | Should update status to ACCEPTED when everything is correct")
  @Test
  void updateApplicationStatus_shouldSetStatusToAcceptedAndLinkUser_whenDecisionIsToAccept() {

    long mentorId = 1L;
    long studentId = 10L;
    long applicationId = 100L;

    ApplicationDecisionRequestDto decisionDto = new ApplicationDecisionRequestDto();
    decisionDto.setAccepted(true);

    Mentor mentor = new Mentor();
    mentor.setId(mentorId);

    Student student = new Student();
    student.setId(studentId);

    Application mainApplication = new Application();
    mainApplication.setId(applicationId);
    mainApplication.setStatus(ApplicationStatus.PENDING);
    mainApplication.setMentor(mentor);
    mainApplication.setStudent(student);

    Application otherApp1 = new Application();
    otherApp1.setId(101L);
    otherApp1.setStatus(ApplicationStatus.PENDING);

    Application otherApp2 = new Application();
    otherApp2.setId(102L);
    otherApp2.setStatus(ApplicationStatus.PENDING);

    List<Application> otherPendingApps = List.of(otherApp1, otherApp2);

    when(userFinder.findMentorById(mentorId)).thenReturn(mentor);
    when(applicationRepository.findById(applicationId)).thenReturn(Optional.of(mainApplication));

    when(applicationRepository.findAllByStudentIdAndStatus(studentId, ApplicationStatus.PENDING))
        .thenReturn(otherPendingApps);

    mentorshipServiceImpl.updateApplicationStatus(mentorId, applicationId, decisionDto);

    verify(mentorshipLifecycleService, times(1)).establishLink(mentor, student);

    assertEquals(ApplicationStatus.ACCEPTED, mainApplication.getStatus());

    assertEquals(ApplicationStatus.EXPIRED, otherApp1.getStatus());
    assertEquals(ApplicationStatus.EXPIRED, otherApp2.getStatus());

    verify(applicationRepository, times(1)).save(mainApplication);

    verify(applicationRepository, times(1)).saveAll(otherPendingApps);

    verify(userFinder, times(1)).findMentorById(mentorId);
    verify(applicationRepository, times(1)).findById(applicationId);
    verify(applicationRepository, times(1))
        .findAllByStudentIdAndStatus(studentId, ApplicationStatus.PENDING);
  }

  @DisplayName(
      "updateApplicationStatus | Should update status to REJECTED when application is pending and rejected")
  @Test
  void updateApplicationStatus_updatesToRejected_whenApplicationIsPendingAndRejected() {
    Long mentorId = 1L;
    Long applicationId = 2L;
    ApplicationDecisionRequestDto dto = new ApplicationDecisionRequestDto();
    dto.setAccepted(false);

    Mentor mentor = new Mentor();
    mentor.setId(mentorId);

    Application application = new Application();
    application.setId(applicationId);
    application.setStatus(ApplicationStatus.PENDING);
    application.setMentor(mentor);

    when(userFinder.findMentorById(mentorId)).thenReturn(mentor);
    when(applicationRepository.findById(applicationId))
        .thenReturn(java.util.Optional.of(application));

    mentorshipServiceImpl.updateApplicationStatus(mentorId, applicationId, dto);

    verify(userFinder, times(1)).findMentorById(mentorId);
    verify(applicationRepository, times(1)).findById(applicationId);
    verify(applicationRepository, times(1)).save(application);
    assertEquals(ApplicationStatus.REJECTED, application.getStatus());
  }

  @DisplayName("updateApplicationStatus | Should throw exception when application is not pending")
  @Test
  void updateApplicationStatus_throwsException_whenApplicationIsNotPending() {
    Long mentorId = 1L;
    Long applicationId = 2L;
    ApplicationDecisionRequestDto dto = new ApplicationDecisionRequestDto();
    dto.setAccepted(true);

    Mentor mentor = new Mentor();
    mentor.setId(mentorId);

    Application application = new Application();
    application.setId(applicationId);
    application.setStatus(ApplicationStatus.ACCEPTED);
    application.setMentor(mentor);

    when(userFinder.findMentorById(mentorId)).thenReturn(mentor);
    when(applicationRepository.findById(applicationId))
        .thenReturn(java.util.Optional.of(application));

    assertThrows(
        IllegalStateException.class,
        () -> mentorshipServiceImpl.updateApplicationStatus(mentorId, applicationId, dto));

    verify(userFinder, times(1)).findMentorById(mentorId);
    verify(applicationRepository, times(1)).findById(applicationId);
    verifyNoMoreInteractions(applicationRepository);
  }

  @DisplayName(
      "updateApplicationStatus | Should throw exception when mentor is not the owner of the application")
  @Test
  void updateApplicationStatus_throwsException_whenMentorIsNotOwnerOfApplication() {
    Long mentorId = 1L;
    Long applicationId = 2L;
    ApplicationDecisionRequestDto dto = new ApplicationDecisionRequestDto();
    dto.setAccepted(true);

    Mentor mentor = new Mentor();
    mentor.setId(mentorId);

    Mentor otherMentor = new Mentor();
    otherMentor.setId(3L);

    Application application = new Application();
    application.setId(applicationId);
    application.setStatus(ApplicationStatus.PENDING);
    application.setMentor(otherMentor);

    when(userFinder.findMentorById(mentorId)).thenReturn(mentor);
    when(applicationRepository.findById(applicationId))
        .thenReturn(java.util.Optional.of(application));

    assertThrows(
        SecurityException.class,
        () -> mentorshipServiceImpl.updateApplicationStatus(mentorId, applicationId, dto));

    verify(userFinder, times(1)).findMentorById(mentorId);
    verify(applicationRepository, times(1)).findById(applicationId);
    verifyNoMoreInteractions(applicationRepository);
  }

  @DisplayName(
      "updateApplicationStatus | Should expire other pending applications when application is accepted")
  @Test
  void updateApplicationStatus_expiresOtherPendingApplications_whenApplicationIsAccepted() {
    Long mentorId = 1L;
    Long applicationId = 2L;
    ApplicationDecisionRequestDto dto = new ApplicationDecisionRequestDto();
    dto.setAccepted(true);

    Mentor mentor = new Mentor();
    mentor.setId(mentorId);

    Student student = new Student();
    student.setId(3L);

    Application application = new Application();
    application.setId(applicationId);
    application.setStatus(ApplicationStatus.PENDING);
    application.setMentor(mentor);
    application.setStudent(student);

    Application otherPendingApplication = new Application();
    otherPendingApplication.setId(4L);
    otherPendingApplication.setStatus(ApplicationStatus.PENDING);
    otherPendingApplication.setStudent(student);

    when(userFinder.findMentorById(mentorId)).thenReturn(mentor);
    when(applicationRepository.findById(applicationId))
        .thenReturn(java.util.Optional.of(application));
    when(applicationRepository.findAllByStudentIdAndStatus(
            student.getId(), ApplicationStatus.PENDING))
        .thenReturn(List.of(otherPendingApplication));

    mentorshipServiceImpl.updateApplicationStatus(mentorId, applicationId, dto);

    verify(userFinder, times(1)).findMentorById(mentorId);
    verify(applicationRepository, times(1)).findById(applicationId);
    verify(applicationRepository, times(1))
        .findAllByStudentIdAndStatus(student.getId(), ApplicationStatus.PENDING);
    verify(applicationRepository, times(1)).save(application);
    verify(applicationRepository, times(1)).saveAll(List.of(otherPendingApplication));
    assertEquals(ApplicationStatus.EXPIRED, otherPendingApplication.getStatus());
  }

  @DisplayName("updateApplicationStatus | Should throw exception when application does not exist")
  @Test
  void updateApplicationStatus_throwsException_whenApplicationDoesNotExist() {
    Long mentorId = 1L;
    Long applicationId = 2L;
    ApplicationDecisionRequestDto dto = new ApplicationDecisionRequestDto();
    dto.setAccepted(true);

    Mentor mentor = new Mentor();
    mentor.setId(mentorId);

    when(userFinder.findMentorById(mentorId)).thenReturn(mentor);
    when(applicationRepository.findById(applicationId)).thenReturn(java.util.Optional.empty());

    assertThrows(
        ResourceNotFoundException.class,
        () -> mentorshipServiceImpl.updateApplicationStatus(mentorId, applicationId, dto));

    verify(userFinder, times(1)).findMentorById(mentorId);
    verify(applicationRepository, times(1)).findById(applicationId);
    verifyNoMoreInteractions(applicationRepository);
  }
}
