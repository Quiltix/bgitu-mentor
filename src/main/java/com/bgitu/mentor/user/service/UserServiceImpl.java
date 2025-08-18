package com.bgitu.mentor.user.service;

import com.bgitu.mentor.user.data.repository.BaseUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final BaseUserRepository baseUserRepository;

  @Override
  public boolean existsByEmail(String email) {
    return baseUserRepository.existsByEmail(email);
  }
}
