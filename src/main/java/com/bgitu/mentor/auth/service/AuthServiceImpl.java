package com.bgitu.mentor.auth.service;

import com.bgitu.mentor.auth.Role;
import com.bgitu.mentor.auth.dto.JwtAuthenticationResponseDto;
import com.bgitu.mentor.auth.dto.LoginRequestDto;
import com.bgitu.mentor.auth.dto.RegisterRequestDto;
import com.bgitu.mentor.auth.security.JwtTokenProvider;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.student.data.model.Student;
import com.bgitu.mentor.user.data.model.BaseUser;
import com.bgitu.mentor.user.data.repository.BaseUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final BaseUserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

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

        // 2. Возвращаем AuthResult с токеном и уже известной нам ролью
        return new JwtAuthenticationResponseDto(token,userRole.name());
    }


    public JwtAuthenticationResponseDto login(LoginRequestDto dto) {
        String email = dto.getEmail();
        String rawPassword = dto.getPassword();


        BaseUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));


        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Неверный пароль");
        }

        Role role;
        if (user instanceof Mentor) {
            role = Role.MENTOR;
        } else if (user instanceof Student) {
            role = Role.STUDENT;
        } else {
            throw new IllegalStateException("Неопределенная роль для пользователя");
        }
        String token = tokenProvider.generateToken(user.getId(), role);

        return new JwtAuthenticationResponseDto(token,role.name());
    }
}
