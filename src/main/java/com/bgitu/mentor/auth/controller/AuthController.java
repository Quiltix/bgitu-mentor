package com.bgitu.mentor.auth.controller;

import com.bgitu.mentor.auth.dto.JwtAuthenticationResponseDto;
import com.bgitu.mentor.auth.dto.LoginRequestDto;
import com.bgitu.mentor.auth.dto.RegisterRequestDto;
import com.bgitu.mentor.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Сервис авторизации", description = "Регистрирует и авторизует пользователя, выдает jwt токен")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Регистрация пользователя", description = "Регистрирует пользователя в зависимости от роли, выдает jwt токен.")
    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponseDto> register(@RequestBody @Valid RegisterRequestDto requestDto) {
        String token = authService.register(requestDto);
        return ResponseEntity.ok(new JwtAuthenticationResponseDto(token));
    }

    @Operation(summary = "Авторизация пользователя", description = "Моторизирует пользователя в зависимости от роли, выдает jwt токен.")
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponseDto> login(@RequestBody @Valid LoginRequestDto requestDto){
        String token = authService.login(requestDto);
        return ResponseEntity.ok(new JwtAuthenticationResponseDto(token));
    }
}