package com.bgitu.mentor.common.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeAvatar(MultipartFile file, String filenamePrefix) {
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileName = filenamePrefix + "." + extension;
        Path path = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(path);
            Path filePath = path.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return "/api/files/avatars/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Could not store file: " + fileName, e);
        }
    }

    public Resource loadAvatar(String fileName) {
        try {
            Path path = Paths.get(uploadDir).toAbsolutePath().resolve(fileName).normalize();
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists()) return resource;
            else throw new FileNotFoundException("File not found " + fileName);
        } catch (Exception e) {
            throw new RuntimeException("File load failed", e);
        }
    }
}