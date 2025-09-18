package com.bgitu.mentor.auth.service;

import com.bgitu.mentor.auth.Role;
import com.bgitu.mentor.auth.data.AuthMapper;
import com.bgitu.mentor.auth.data.dto.JwtAuthenticationResponseDto;
import com.bgitu.mentor.auth.data.dto.LoginRequestDto;
import com.bgitu.mentor.auth.data.dto.RegisterRequestDto;
import com.bgitu.mentor.auth.security.AuthenticatedUser;
import com.bgitu.mentor.auth.security.JwtTokenProvider;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.student.data.model.Student;
import com.bgitu.mentor.user.data.model.BaseUser;
import com.bgitu.mentor.user.data.repository.BaseUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final PasswordEncoder passwordEncoder;
  private final BaseUserRepository userRepository;
  private final JwtTokenProvider tokenProvider;
  private final AuthenticationManager authenticationManager;
  private final AuthMapper authMapper;

  @Transactional
  public JwtAuthenticationResponseDto register(RegisterRequestDto dto) {
    String email = dto.getEmail();

    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("Email уже используется");
    }

    BaseUser userToSave;
    if (dto.getRole() == Role.MENTOR) {
      Mentor mentor = new Mentor();
      mentor.setRank(0);
      userToSave = mentor;
    } else if (dto.getRole() == Role.STUDENT) {
      userToSave = new Student();
    } else {
      throw new IllegalArgumentException("Вы выбрали недопустимую роль");
    }

    userToSave.setEmail(email);
    userToSave.setPassword(passwordEncoder.encode(dto.getPassword()));
    userToSave.setFirstName(dto.getFirstName());
    userToSave.setLastName(dto.getLastName());

    BaseUser savedUser = userRepository.save(userToSave);

    Role userRole = dto.getRole();

    String token = tokenProvider.generateToken(savedUser.getId(), userRole);

    return authMapper.toDto(token, userRole.name());
  }

  @Override
  @Transactional(readOnly = true)
  public JwtAuthenticationResponseDto login(LoginRequestDto dto) {

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());

    Authentication authentication = authenticationManager.authenticate(authenticationToken);

    AuthenticatedUser userDetails = (AuthenticatedUser) authentication.getPrincipal();

    String token = tokenProvider.generateToken(userDetails.getId(), userDetails.getRole());

    return authMapper.toDto(token, userDetails.getRole().name());
  }
}
