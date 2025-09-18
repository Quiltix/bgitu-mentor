package com.bgitu.mentor.auth.service;

import com.bgitu.mentor.auth.data.dto.JwtAuthenticationResponseDto;
import com.bgitu.mentor.auth.data.dto.LoginRequestDto;
import com.bgitu.mentor.auth.data.dto.RegisterRequestDto;

public interface AuthService {

  JwtAuthenticationResponseDto register(RegisterRequestDto dto);

  JwtAuthenticationResponseDto login(LoginRequestDto dto);
}
