package com.bgitu.mentor.mentorship;

import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentorship.data.ApplicationMapper;
import com.bgitu.mentor.mentorship.data.dto.ApplicationCreateRequestDto;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
}
