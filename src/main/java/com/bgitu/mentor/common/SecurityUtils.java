package com.bgitu.mentor.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public final class SecurityUtils {

    private SecurityUtils() {

    }

    public static Long getCurrentUserId(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            throw new UsernameNotFoundException("Не удалось получить информацию о пользователе из контекста безопасности");
        }

        return Long.parseLong(authentication.getName());

    }
}