package com.bgitu.mentor.user.service;

import com.bgitu.mentor.common.dto.UserCredentialsUpdateRequestDto;
import com.bgitu.mentor.user.data.dto.BaseUserUpdateRequestDto;
import com.bgitu.mentor.user.data.model.BaseUser;
import org.springframework.web.multipart.MultipartFile;

public interface BaseUserManagementService {

  BaseUser updateProfile(Long userId, UserCredentialsUpdateRequestDto dto);

  void updateCard(BaseUser user, BaseUserUpdateRequestDto dto, MultipartFile avatarFile);
}
