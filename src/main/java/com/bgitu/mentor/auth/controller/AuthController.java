package com.bgitu.mentor.auth.controller;

import com.bgitu.mentor.auth.dto.JwtAuthenticationResponseDto;
import com.bgitu.mentor.auth.dto.LoginRequestDto;
import com.bgitu.mentor.auth.dto.RegisterRequestDto;
import com.bgitu.mentor.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "Регистрирует и авторизует пользователя, выдает jwt токен")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED) // <-- Используем правильный статус 201 для создания
  @Operation(summary = "Регистрация нового пользователя")
  public JwtAuthenticationResponseDto register(@RequestBody @Valid RegisterRequestDto requestDto) {

    return authService.register(requestDto);
  }

  @PostMapping("/login")
  @Operation(summary = "Аутентификация пользователя")
  public JwtAuthenticationResponseDto login(@RequestBody @Valid LoginRequestDto requestDto) {

    return authService.login(requestDto);
  }
}
