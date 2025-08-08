package com.bgitu.mentor.user.service;

import com.bgitu.mentor.user.data.model.BaseUser;
import com.bgitu.mentor.user.data.repository.BaseUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final BaseUserRepository baseUserRepository;

  @Override
  public BaseUser findById(Long userId) {
    return baseUserRepository
        .findById(userId)
        .orElseThrow(
            () -> new EntityNotFoundException("Пользователь с id=" + userId + " не найден"));
  }

  @Override
  public boolean existsByEmail(String email) {
    return baseUserRepository.existsByEmail(email);
  }
}
