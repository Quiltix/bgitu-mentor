package com.bgitu.mentor.user.service;

import com.bgitu.mentor.user.data.dto.UserCredentialsUpdateRequestDto;
import com.bgitu.mentor.file.service.FileStorageService;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.user.data.dto.BaseUserUpdateRequestDto;
import com.bgitu.mentor.user.data.model.BaseUser;
import com.bgitu.mentor.user.data.repository.BaseUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BaseUserManagementServiceImpl implements BaseUserManagementService {

  private final UserService userService;
  private final UserFinder userFinder;
  private final PasswordEncoder passwordEncoder;
  private final FileStorageService fileStorageService;
  private final BaseUserRepository baseUserRepository;
  private static final String DEFAULT_AVATAR_URL = "/api/uploads/image/";

  @Override
  @Transactional
  public BaseUser updateProfile(Long userId, UserCredentialsUpdateRequestDto dto) {
    BaseUser user = userFinder.findUserById(userId);

    String newEmail = dto.getEmail();
    if (newEmail != null && !newEmail.isBlank() && !newEmail.equalsIgnoreCase(user.getEmail())) {
      if (userService.existsByEmail(newEmail)) {
        throw new IllegalArgumentException("Этот email уже занят другим пользователем.");
      }
      user.setEmail(newEmail.toLowerCase());
    }

    if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
      user.setPassword(passwordEncoder.encode(dto.getPassword()));
    }

    return baseUserRepository.save(user);
  }

  @Override
  @Transactional
  public void updateCard(BaseUser user, BaseUserUpdateRequestDto dto, MultipartFile avatarFile) {
    if (dto.getFirstName() != null) {
      user.setFirstName(dto.getFirstName());
    }
    if (dto.getLastName() != null) {
      user.setLastName(dto.getLastName());
    }
    if (dto.getDescription() != null) {
      user.setDescription(dto.getDescription());
    }
    if (dto.getTelegramUrl() != null) {
      user.setTelegramUrl(dto.getTelegramUrl());
    }
    if (dto.getVkUrl() != null) {
      user.setVkUrl(dto.getVkUrl());
    }

    if (avatarFile != null && !avatarFile.isEmpty()) {

      String oldAvatarRelativePath = extractRelativePath(user.getAvatarUrl());

      String userType = (user instanceof Mentor) ? "mentors" : "students";
      String newAvatarRelativePath = fileStorageService.store(avatarFile, userType + "/avatars");

      user.setAvatarUrl(DEFAULT_AVATAR_URL + newAvatarRelativePath.replace("\\", "/"));

      if (oldAvatarRelativePath != null) {
        fileStorageService.delete(oldAvatarRelativePath);
      }
    }
  }

  private String extractRelativePath(String fullUrl) {
    if (fullUrl == null || !fullUrl.contains(DEFAULT_AVATAR_URL)) {
      return null;
    }
    return fullUrl.substring(fullUrl.indexOf(DEFAULT_AVATAR_URL) + DEFAULT_AVATAR_URL.length());
  }
}
