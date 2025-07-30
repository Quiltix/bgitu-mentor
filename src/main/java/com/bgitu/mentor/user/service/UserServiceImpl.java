package com.bgitu.mentor.user.service;

import com.bgitu.mentor.common.SecurityUtils;
import com.bgitu.mentor.user.model.BaseUser;
import com.bgitu.mentor.user.repository.BaseUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final BaseUserRepository baseUserRepository;

    @Override
    public BaseUser findById(Long userId) {
        return baseUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с id=" + userId + " не найден"));
    }

    @Override
    public BaseUser getByAuth(Authentication authentication) {
        Long userId = SecurityUtils.getCurrentUserId(authentication);
        return findById(userId);
    }
    @Override
    public boolean existsByEmail(String email) {
        return baseUserRepository.existsByEmail(email);
    }
}

