package com.bgitu.mentor.user.service;

import com.bgitu.mentor.common.dto.UserCredentialsUpdateRequestDto;
import com.bgitu.mentor.common.service.FileStorageService;
import com.bgitu.mentor.student.data.model.Student;
import com.bgitu.mentor.user.data.model.BaseUser;
import com.bgitu.mentor.user.data.repository.BaseUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BaseUserManagementServiceTest {

  @Mock private UserService userService;
  @Mock private UserFinder userFinder;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private FileStorageService fileStorageService;
  @Mock private BaseUserRepository baseUserRepository;

  @InjectMocks private BaseUserManagementServiceImpl baseUserManagementService;

  private BaseUser existingUser;

  @BeforeEach
  void setUp() {
    existingUser = new Student(); // Используем конкретную реализацию, это проще
    existingUser.setId(1L);
    existingUser.setEmail("1@ya.ru");
    existingUser.setPassword("hashedOldPassword");
  }

  @Test
  @DisplayName(
      "updateProfile | Должен обновить email и пароль, если переданы новые валидные данные")
  void updateProfile_shouldUpdateEmailAndPassword_whenDataIsValid() {

    long userId = 1L;
    String newEmail = "nEw.email@example.com";
    String newPassword = "newPassword123";
    String newHashedPassword = "hashedNewPassword";

    UserCredentialsUpdateRequestDto dto = new UserCredentialsUpdateRequestDto();
    dto.setEmail(newEmail);
    dto.setPassword(newPassword);

    when(userFinder.findUserById(userId)).thenReturn(existingUser);
    when(userService.existsByEmail(newEmail)).thenReturn(false);
    when(passwordEncoder.encode(newPassword)).thenReturn(newHashedPassword);
    when(baseUserRepository.save(any(BaseUser.class))).thenAnswer(inv -> inv.getArgument(0));

    BaseUser updatedBaseUser = baseUserManagementService.updateProfile(userId, dto);

    assertNotNull(updatedBaseUser);
    assertEquals(newEmail.toLowerCase(), updatedBaseUser.getEmail());
    assertEquals(newHashedPassword, updatedBaseUser.getPassword());

    verify(userService, times(1)).existsByEmail(newEmail);
    verify(passwordEncoder, times(1)).encode(newPassword);
    verify(baseUserRepository, times(1)).save(existingUser);
  }
}
