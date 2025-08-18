package com.bgitu.mentor.auth;

import com.bgitu.mentor.auth.dto.JwtAuthenticationResponseDto;
import com.bgitu.mentor.auth.dto.RegisterRequestDto;
import com.bgitu.mentor.auth.security.JwtTokenProvider;
import com.bgitu.mentor.auth.service.AuthServiceImpl;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.student.data.model.Student;
import com.bgitu.mentor.user.data.repository.BaseUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

  @Mock private PasswordEncoder passwordEncoder;
  @Mock private BaseUserRepository userRepository;
  @Mock private JwtTokenProvider tokenProvider;
  @Mock private AuthenticationManager authenticationManager;

  @InjectMocks private AuthServiceImpl authServiceImpl;

  @DisplayName("register | Should throw exception when email already exists")
  @Test
  void register_throwsException_whenEmailAlreadyExists() {
    RegisterRequestDto dto = new RegisterRequestDto();
    dto.setEmail("existing@example.com");
    when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

    assertThrows(IllegalArgumentException.class, () -> authServiceImpl.register(dto));

    verify(userRepository, times(1)).existsByEmail(dto.getEmail());
    verifyNoMoreInteractions(userRepository);
  }

  @DisplayName("register | Should save mentor when role is MENTOR")
  @Test
  void register_savesMentor_whenRoleIsMentor() {
    RegisterRequestDto dto = new RegisterRequestDto();
    dto.setEmail("mentor@example.com");
    dto.setPassword("password");
    dto.setFirstName("John");
    dto.setLastName("Doe");
    dto.setRole(Role.MENTOR);

    Mentor savedMentor = new Mentor();
    savedMentor.setId(1L);
    when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
    when(userRepository.save(any(Mentor.class))).thenReturn(savedMentor);
    when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
    when(tokenProvider.generateToken(savedMentor.getId(), Role.MENTOR)).thenReturn("jwtToken");

    JwtAuthenticationResponseDto response = authServiceImpl.register(dto);

    verify(userRepository, times(1)).existsByEmail(dto.getEmail());
    verify(userRepository, times(1)).save(any(Mentor.class));
    verify(passwordEncoder, times(1)).encode(dto.getPassword());
    verify(tokenProvider, times(1)).generateToken(savedMentor.getId(), Role.MENTOR);
    assertEquals("jwtToken", response.getAccessToken());
    assertEquals(Role.MENTOR.name(), response.getRole());
  }

  @DisplayName("register | Should save student when role is STUDENT")
  @Test
  void register_savesStudent_whenRoleIsStudent() {
    RegisterRequestDto dto = new RegisterRequestDto();
    dto.setEmail("student@example.com");
    dto.setPassword("password");
    dto.setFirstName("Jane");
    dto.setLastName("Doe");
    dto.setRole(Role.STUDENT);

    Student savedStudent = new Student();
    savedStudent.setId(2L);
    when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
    when(userRepository.save(any(Student.class))).thenReturn(savedStudent);
    when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");
    when(tokenProvider.generateToken(savedStudent.getId(), Role.STUDENT)).thenReturn("jwtToken");

    JwtAuthenticationResponseDto response = authServiceImpl.register(dto);

    verify(userRepository, times(1)).existsByEmail(dto.getEmail());
    verify(userRepository, times(1)).save(any(Student.class));
    verify(passwordEncoder, times(1)).encode(dto.getPassword());
    verify(tokenProvider, times(1)).generateToken(savedStudent.getId(), Role.STUDENT);
    assertEquals("jwtToken", response.getAccessToken());
    assertEquals(Role.STUDENT.name(), response.getRole());
  }

  @DisplayName("register | Should throw exception when role is invalid")
  @Test
  void register_throwsException_whenRoleIsInvalid() {
    RegisterRequestDto dto = new RegisterRequestDto();
    dto.setEmail("invalid@example.com");
    dto.setRole(null);

    when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);

    assertThrows(IllegalArgumentException.class, () -> authServiceImpl.register(dto));

    verify(userRepository, times(1)).existsByEmail(dto.getEmail());
    verifyNoMoreInteractions(userRepository);
  }
}
