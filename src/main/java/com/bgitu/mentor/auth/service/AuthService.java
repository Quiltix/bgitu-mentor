package com.bgitu.mentor.auth.service;

import com.bgitu.mentor.auth.Role;
import com.bgitu.mentor.auth.dto.LoginRequestDto;
import com.bgitu.mentor.auth.dto.RegisterRequestDto;
import com.bgitu.mentor.auth.security.JwtTokenProvider;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.mentor.data.repository.MentorRepository;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.repository.StudentRepository;
import com.bgitu.mentor.user.model.BaseUser;
import com.bgitu.mentor.user.repository.BaseUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MentorRepository mentorRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final BaseUserRepository userRepository;
    private final JwtTokenProvider tokenProvider;

    public String register(RegisterRequestDto dto) {
        String email = dto.getEmail();

        // Одна простая проверка вместо двух
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


        return tokenProvider.generateToken(savedUser.getId(), dto.getRole());
    }


    public String login(LoginRequestDto dto) {
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

        return tokenProvider.generateToken(user.getId(), role);
    }
}
