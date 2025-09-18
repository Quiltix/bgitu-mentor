package com.bgitu.mentor.auth.data;

import com.bgitu.mentor.auth.data.dto.JwtAuthenticationResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

  JwtAuthenticationResponseDto toDto(String accessToken, String role);
}
