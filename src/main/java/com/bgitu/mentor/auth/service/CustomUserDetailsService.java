package com.bgitu.mentor.auth.service;

import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.user.model.BaseUser;
import com.bgitu.mentor.user.repository.BaseUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final BaseUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userIdString) throws UsernameNotFoundException {
        Long userId;
        try {
            userId = Long.parseLong(userIdString);
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("Некорректный формат пользователя: " + userIdString);
        }

        BaseUser user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с ID: " + userId + " не найден"));

        String role = (user instanceof Mentor) ? "ROLE_MENTOR" : "ROLE_STUDENT";

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}