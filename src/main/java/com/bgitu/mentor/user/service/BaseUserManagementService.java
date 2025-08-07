// src/main/java/com/bgitu/mentor/user/service/BaseUserManagementService.java
package com.bgitu.mentor.user.service;

import com.bgitu.mentor.common.dto.UserCredentialsUpdateRequestDto;
import com.bgitu.mentor.common.service.FileStorageService;
import com.bgitu.mentor.mentor.data.model.Mentor;
import com.bgitu.mentor.user.data.dto.BaseUserUpdateRequestDto;
import com.bgitu.mentor.user.data.model.BaseUser;
import com.bgitu.mentor.user.data.repository.BaseUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BaseUserManagementService {

    private final UserService userService;
    private final UserFinder userFinder;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;
    private final BaseUserRepository baseUserRepository;

    public void updateProfile(Long userId, UserCredentialsUpdateRequestDto dto) {
        BaseUser user = userFinder.findUserById(userId);

        String newEmail = dto.getEmail();
        if (newEmail != null && !newEmail.isBlank() && !newEmail.equalsIgnoreCase(user.getEmail())) {
            if (userService.existsByEmail(newEmail)) {
                throw new IllegalArgumentException("Этот email уже занят другим пользователем.");
            }
            user.setEmail(newEmail);
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        baseUserRepository.save(user);
    }

    public void updateCard(BaseUser user, BaseUserUpdateRequestDto dto, MultipartFile avatarFile) {
        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getDescription() != null) user.setDescription(dto.getDescription());
        if (dto.getTelegramUrl() != null) user.setTelegramUrl(dto.getTelegramUrl());
        if (dto.getVkUrl() != null) user.setVkUrl(dto.getVkUrl());

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String userType = (user instanceof Mentor) ? "mentor" : "student";
            String storedRelativePath = fileStorageService.store(avatarFile, userType + "s/avatars");
            user.setAvatarUrl("/api/uploads/image/" + storedRelativePath.replace("\\", "/"));
        }
    }
}