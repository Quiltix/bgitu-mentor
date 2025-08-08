package com.bgitu.mentor.auth.service;

import com.bgitu.mentor.auth.dto.JwtAuthenticationResponseDto;
import com.bgitu.mentor.auth.dto.LoginRequestDto;
import com.bgitu.mentor.auth.dto.RegisterRequestDto;

public interface AuthService {

  JwtAuthenticationResponseDto register(RegisterRequestDto dto);

  JwtAuthenticationResponseDto login(LoginRequestDto dto);
}
