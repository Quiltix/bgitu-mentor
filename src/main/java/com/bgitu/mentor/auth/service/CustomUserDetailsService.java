package com.bgitu.mentor.auth.service;

import com.bgitu.mentor.mentor.model.Mentor;
import com.bgitu.mentor.mentor.repository.MentorRepository;
import com.bgitu.mentor.student.model.Student;
import com.bgitu.mentor.student.repository.StudentRepository;
import com.bgitu.mentor.user.model.BaseUser;
import com.bgitu.mentor.user.repository.BaseUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final BaseUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        BaseUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с email: " + email + " не найден"));

        String role = null;
        if (user instanceof Mentor) {
            role = "ROLE_MENTOR";
        } else if (user instanceof Student) {
            role = "ROLE_STUDENT";
        } else {
            throw new IllegalStateException("Неопределенная роль для пользователя " + email);
        }

        return new User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}
