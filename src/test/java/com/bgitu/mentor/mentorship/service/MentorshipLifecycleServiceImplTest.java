package com.bgitu.mentor.mentorship.service;

import com.bgitu.mentor.common.exception.ResourceNotFoundException;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.student.data.model.Student;
import com.bgitu.mentor.student.data.repository.StudentRepository;
import com.bgitu.mentor.user.service.UserFinder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MentorshipLifecycleServiceImplTest {

  @Mock private UserFinder userFinder;
  @Mock private StudentRepository studentRepository;

  @InjectMocks private MentorshipLifecycleServiceImpl mentorshipLifecycleServiceImpl;

  @DisplayName("terminateLinkByMentor | Should terminate link when mentor and student are linked")
  @Test
  void terminateLinkByMentor_terminatesLink_whenMentorAndStudentAreLinked() {
    Long mentorId = 1L;
    Long studentId = 2L;

    Mentor mentor = new Mentor();
    mentor.setId(mentorId);

    Student student = new Student();
    student.setId(studentId);
    student.setMentor(mentor);

    when(userFinder.findMentorById(mentorId)).thenReturn(mentor);
    when(userFinder.findStudentById(studentId)).thenReturn(student);

    mentorshipLifecycleServiceImpl.terminateLinkByMentor(mentorId, studentId);

    verify(userFinder, times(1)).findMentorById(mentorId);
    verify(userFinder, times(1)).findStudentById(studentId);
    verify(studentRepository, times(1)).save(student);
    assertNull(student.getMentor());
  }

  @DisplayName(
      "terminateLinkByMentor | Should throw exception when student is not linked to mentor")
  @Test
  void terminateLinkByMentor_throwsException_whenStudentNotLinkedToMentor() {
    Long mentorId = 1L;
    Long studentId = 2L;

    Mentor mentor = new Mentor();
    mentor.setId(mentorId);

    Mentor anotherMentor = new Mentor();
    anotherMentor.setId(3L);

    Student student = new Student();
    student.setId(studentId);
    student.setMentor(anotherMentor);

    when(userFinder.findMentorById(mentorId)).thenReturn(mentor);
    when(userFinder.findStudentById(studentId)).thenReturn(student);

    assertThrows(
        SecurityException.class,
        () -> mentorshipLifecycleServiceImpl.terminateLinkByMentor(mentorId, studentId));

    verify(userFinder, times(1)).findMentorById(mentorId);
    verify(userFinder, times(1)).findStudentById(studentId);
    verifyNoInteractions(studentRepository);
  }

  @DisplayName("terminateLinkByMentor | Should throw exception when student has no mentor")
  @Test
  void terminateLinkByMentor_throwsException_whenStudentHasNoMentor() {
    Long mentorId = 1L;
    Long studentId = 2L;

    Mentor mentor = new Mentor();
    mentor.setId(mentorId);

    Student student = new Student();
    student.setId(studentId);
    student.setMentor(null);

    when(userFinder.findMentorById(mentorId)).thenReturn(mentor);
    when(userFinder.findStudentById(studentId)).thenReturn(student);

    assertThrows(
        SecurityException.class,
        () -> mentorshipLifecycleServiceImpl.terminateLinkByMentor(mentorId, studentId));

    verify(userFinder, times(1)).findMentorById(mentorId);
    verify(userFinder, times(1)).findStudentById(studentId);
    verifyNoInteractions(studentRepository);
  }

  @DisplayName("establishLink | Should establish link when student has no mentor")
  @Test
  void establishLink_establishesLink_whenStudentHasNoMentor() {
    Mentor mentor = new Mentor();
    mentor.setId(1L);

    Student student = new Student();
    student.setId(2L);
    student.setMentor(null);

    mentorshipLifecycleServiceImpl.establishLink(mentor, student);

    assertEquals(mentor, student.getMentor());
    verify(studentRepository, times(1)).save(student);
  }

  @DisplayName("establishLink | Should throw exception when student already has a mentor")
  @Test
  void establishLink_throwsException_whenStudentAlreadyHasMentor() {
    Mentor mentor = new Mentor();
    mentor.setId(1L);

    Student student = new Student();
    student.setId(2L);
    student.setMentor(new Mentor());

    assertThrows(
        IllegalStateException.class,
        () -> mentorshipLifecycleServiceImpl.establishLink(mentor, student));

    verifyNoInteractions(studentRepository);
  }

  @DisplayName("terminateLinkByStudent | Should terminate link when student has a mentor")
  @Test
  void terminateLinkByStudent_terminatesLink_whenStudentHasMentor() {
    Long studentId = 1L;

    Mentor mentor = new Mentor();
    mentor.setId(2L);

    Student student = new Student();
    student.setId(studentId);
    student.setMentor(mentor);

    when(userFinder.findStudentById(studentId)).thenReturn(student);

    mentorshipLifecycleServiceImpl.terminateLinkByStudent(studentId);

    verify(userFinder, times(1)).findStudentById(studentId);
    verify(studentRepository, times(1)).save(student);
    assertNull(student.getMentor());
  }

  @DisplayName("terminateLinkByStudent | Should throw exception when student has no mentor")
  @Test
  void terminateLinkByStudent_throwsException_whenStudentHasNoMentor() {
    Long studentId = 1L;

    Student student = new Student();
    student.setId(studentId);
    student.setMentor(null);

    when(userFinder.findStudentById(studentId)).thenReturn(student);

    assertThrows(
        ResourceNotFoundException.class,
        () -> mentorshipLifecycleServiceImpl.terminateLinkByStudent(studentId));

    verify(userFinder, times(1)).findStudentById(studentId);
    verifyNoInteractions(studentRepository);
  }
}
