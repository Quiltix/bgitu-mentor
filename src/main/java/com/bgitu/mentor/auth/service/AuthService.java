package com.bgitu.mentor.auth.service;

import com.bgitu.mentor.auth.Role;
import com.bgitu.mentor.auth.dto.LoginRequestDto;
import com.bgitu.mentor.auth.dto.RegisterRequestDto;
import com.bgitu.mentor.auth.security.JwtTokenProvider;
import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.mentor.repository.MentorRepository;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MentorRepository mentorRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public String register(RegisterRequestDto dto) {
        String email = dto.getEmail();

        if (mentorRepository.existsByEmail(email) || studentRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email уже используется");
        }

        if (dto.getRole() == Role.MENTOR) {
            Mentor mentor = new Mentor();
            mentor.setEmail(email);
            mentor.setPassword(passwordEncoder.encode(dto.getPassword()));
            mentor.setFirstName(dto.getFirstName());
            mentor.setLastName(dto.getLastName());
            mentorRepository.save(mentor);
        } else {
            Student student = new Student();
            student.setEmail(email);
            student.setPassword(passwordEncoder.encode(dto.getPassword()));
            student.setFirstName(dto.getFirstName());
            student.setLastName(dto.getLastName());
            studentRepository.save(student);
        }

        return tokenProvider.generateToken(email, dto.getRole());
    }


    public String login(LoginRequestDto dto) {
        String email = dto.getEmail();
        String rawPassword = dto.getPassword();

        Optional<Mentor> mentorOpt = mentorRepository.findByEmail(email);
        if (mentorOpt.isPresent()) {
            Mentor mentor = mentorOpt.get();
            if (!passwordEncoder.matches(rawPassword, mentor.getPassword())) {
                throw new BadCredentialsException("Неверный пароль");
            }
            return tokenProvider.generateToken(email, Role.MENTOR);
        }

        Optional<Student> studentOpt = studentRepository.findByEmail(email);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (!passwordEncoder.matches(rawPassword, student.getPassword())) {
                throw new BadCredentialsException("Неверный пароль");
            }
            return tokenProvider.generateToken(email, Role.STUDENT);
        }

        throw new UsernameNotFoundException("Пользователь не найден");
    }
}
