package com.bgitu.mentor.auth.service;

import com.bgitu.mentor.auth.Role;
import com.bgitu.mentor.auth.security.AuthenticatedUser;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.student.data.model.Student;
import com.bgitu.mentor.user.data.model.BaseUser;
import com.bgitu.mentor.user.data.repository.BaseUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final BaseUserRepository userRepository;

    /**
     * Загружает данные пользователя по его уникальному имени (в данном случае, email).
     * Этот метод вызывается Spring Security AuthenticationManager во время
     * процесса аутентификации по логину и паролю.
     * @param email Email пользователя, который пытается войти.
     * @return UserDetails объект с данными пользователя.
     * @throws UsernameNotFoundException если пользователь с таким email не найден.
     */
    @Override
    @Transactional // Важно для ленивой загрузки, если она понадобится
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. ИЩЕМ ПОЛЬЗОВАТЕЛЯ ПО EMAIL
        BaseUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с email: " + email + " не найден"));

        // 2. ОПРЕДЕЛЯЕМ ЕГО РОЛЬ
        Role role;
        if (user instanceof Mentor) {
            role = Role.MENTOR;
        } else if (user instanceof Student) {
            role = Role.STUDENT;
        } else {
            // Эта ветка не должна быть достижима, но это хорошая защитная мера
            throw new IllegalStateException("Для пользователя с id=" + user.getId() + " не определена конкретная роль (Ментор/Студент).");
        }

        // 3. СОЗДАЕМ И ВОЗВРАЩАЕМ НАШ ТИПИЗИРОВАННЫЙ Principal
        return new AuthenticatedUser(user.getId(), user.getEmail(), user.getPassword(), role);
    }
}