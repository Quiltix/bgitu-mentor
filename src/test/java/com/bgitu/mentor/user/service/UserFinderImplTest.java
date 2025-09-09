package com.bgitu.mentor.user.service;

import com.bgitu.mentor.exception.dto.ResourceNotFoundException;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import com.bgitu.mentor.student.data.model.Student;
import com.bgitu.mentor.student.data.repository.StudentRepository;
import com.bgitu.mentor.user.data.model.BaseUser;
import com.bgitu.mentor.user.data.repository.BaseUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFinderImplTest {

  @Mock BaseUserRepository baseUserRepository;
  @Mock MentorRepository mentorRepository;
  @Mock StudentRepository studentRepository;

  @InjectMocks UserFinderImpl userFinderImpl;

  @DisplayName("findUserById | Should return user when user exists")
  @Test
  void findUserById_returnsUser_whenUserExists() {
    Long userId = 1L;
    BaseUser user = new Student();
    user.setId(userId);

    when(baseUserRepository.findById(userId)).thenReturn(Optional.of(user));

    BaseUser result = userFinderImpl.findUserById(userId);

    verify(baseUserRepository, times(1)).findById(userId);
    assertNotNull(result);
    assertEquals(userId, result.getId());
  }

  @DisplayName("findUserById | Should throw exception when user does not exist")
  @Test
  void findUserById_throwsException_whenUserDoesNotExist() {
    Long userId = 1L;

    when(baseUserRepository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> userFinderImpl.findUserById(userId));

    verify(baseUserRepository, times(1)).findById(userId);
  }

  @DisplayName("findMentorById | Should return mentor when mentor exists")
  @Test
  void findMentorById_returnsMentor_whenMentorExists() {
    Long mentorId = 1L;
    Mentor mentor = new Mentor();
    mentor.setId(mentorId);

    when(mentorRepository.findById(mentorId)).thenReturn(Optional.of(mentor));

    Mentor result = userFinderImpl.findMentorById(mentorId);

    verify(mentorRepository, times(1)).findById(mentorId);
    assertNotNull(result);
    assertEquals(mentorId, result.getId());
  }

  @DisplayName("findMentorById | Should throw exception when mentor does not exist")
  @Test
  void findMentorById_throwsException_whenMentorDoesNotExist() {
    Long mentorId = 1L;

    when(mentorRepository.findById(mentorId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> userFinderImpl.findMentorById(mentorId));

    verify(mentorRepository, times(1)).findById(mentorId);
  }

  @DisplayName("findStudentById | Should return student when student exists")
  @Test
  void findStudentById_returnsStudent_whenStudentExists() {
    Long studentId = 1L;
    Student student = new Student();
    student.setId(studentId);

    when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

    Student result = userFinderImpl.findStudentById(studentId);

    verify(studentRepository, times(1)).findById(studentId);
    assertNotNull(result);
    assertEquals(studentId, result.getId());
  }

  @DisplayName("findStudentById | Should throw exception when student does not exist")
  @Test
  void findStudentById_throwsException_whenStudentDoesNotExist() {
    Long studentId = 1L;

    when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> userFinderImpl.findStudentById(studentId));

    verify(studentRepository, times(1)).findById(studentId);
  }
}
