package com.bgitu.mentor.user.service;

import com.bgitu.mentor.user.data.model.BaseUser;

public interface UserService {

  BaseUser findById(Long userId);

  boolean existsByEmail(String email);
}
