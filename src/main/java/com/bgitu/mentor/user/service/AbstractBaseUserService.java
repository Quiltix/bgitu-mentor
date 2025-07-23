package com.bgitu.mentor.user.service;

import com.bgitu.mentor.common.SecurityUtils;
import com.bgitu.mentor.common.dto.UpdatePersonalInfo;
import com.bgitu.mentor.common.service.FileStorageService;
import com.bgitu.mentor.user.dto.UpdateBaseUserCardDto;
import com.bgitu.mentor.user.model.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

public abstract class AbstractBaseUserService<
        T extends BaseUser,
        R extends JpaRepository<T, Long>
        > {

    protected final R repository;
    protected final PasswordEncoder passwordEncoder;
    protected final FileStorageService fileStorageService;
    private final String userTypeName;

    protected AbstractBaseUserService(R repository, PasswordEncoder passwordEncoder, FileStorageService fileStorageService, String userTypeName) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
        this.userTypeName = userTypeName;
    }

    public T getByAuth(Authentication authentication) {
        Long userId = SecurityUtils.getCurrentUserId(authentication);
        return repository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(userTypeName + " с id=" + userId + " не найден"));
    }

    public T updateProfile(Authentication authentication, UpdatePersonalInfo dto) {
        T user = getByAuth(authentication);

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {

            user.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return repository.save(user);
    }

    protected void updateCardInternal(T user, UpdateBaseUserCardDto dto, MultipartFile avatarFile) {
        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getDescription() != null) user.setDescription(dto.getDescription());
        if (dto.getVkUrl() != null) user.setVkUrl(dto.getVkUrl());
        if (dto.getTelegramUrl() != null) user.setTelegramUrl(dto.getTelegramUrl());

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String storedRelativePath = fileStorageService.store(avatarFile, "avatars");
            String publicUrl = "/api/uploads/image/" + storedRelativePath.replace("\\", "/");
            user.setAvatarUrl(publicUrl);
        }
    }
}