package com.bgitu.mentor.auth.controller;

import com.bgitu.mentor.auth.data.dto.JwtAuthenticationResponseDto;
import com.bgitu.mentor.auth.data.dto.LoginRequestDto;
import com.bgitu.mentor.auth.data.dto.RegisterRequestDto;
import com.bgitu.mentor.auth.service.AuthService;
import com.bgitu.mentor.exception.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Регистрация нового пользователя")
  @ApiResponse(
      responseCode = "201",
      description = "Пользователь успешно зарегистрирован",
      content = @Content(schema = @Schema(implementation = JwtAuthenticationResponseDto.class)))
  @ApiResponse(
      responseCode = "400",
      description =
          "Почта уже используется или выбрана недопустимая роль, также может быть ошибка валидации",
      content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  @ApiResponse(
      responseCode = "500",
      description = "Внутренняя ошибка сервера",
      content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  public JwtAuthenticationResponseDto register(@RequestBody @Valid RegisterRequestDto requestDto) {

    return authService.register(requestDto);
  }

  @PostMapping("/login")
  @Operation(summary = "Аутентификация пользователя")
  @ApiResponse(
      responseCode = "200",
      description = "Пользователь успешно аутентифицирован",
      content = @Content(schema = @Schema(implementation = JwtAuthenticationResponseDto.class)))
  @ApiResponse(
      responseCode = "401",
      description = "Неверный email или пароль",
      content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  @ApiResponse(
      responseCode = "400",
      description = "Ошибка валидации входящих данных(например, пустой email или пароль)",
      content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  @ApiResponse(
      responseCode = "500",
      description = "Внутренняя ошибка сервера",
      content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
  public JwtAuthenticationResponseDto login(@RequestBody @Valid LoginRequestDto requestDto) {

    return authService.login(requestDto);
  }
}
