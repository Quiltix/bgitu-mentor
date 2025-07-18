package com.bgitu.mentor.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Ответ с JWT")
public class JwtAuthenticationResponseDto {
    @Schema(description = "JWT токен", example = "eyJhbGciOiJIUzI1...")
    private String accessToken;
    @Schema(description = "Тип аутентификации", example = "Bearer")
    private final String tokenType = "Bearer";

    private String role;

    public JwtAuthenticationResponseDto(String accessToken, String role) {
        this.accessToken = accessToken;
        this.role = role;

    }
}
