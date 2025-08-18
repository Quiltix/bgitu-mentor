package com.bgitu.mentor.user.service;

import com.bgitu.mentor.common.exception.ResourceNotFoundException;
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
}
