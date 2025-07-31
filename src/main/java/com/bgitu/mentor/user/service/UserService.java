package com.bgitu.mentor.user.service;

import com.bgitu.mentor.user.data.model.BaseUser;
import org.springframework.security.core.Authentication;

public interface UserService {


    BaseUser findById(Long userId);

    boolean existsByEmail(String email);
}