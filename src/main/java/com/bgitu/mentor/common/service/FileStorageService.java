package com.bgitu.mentor.common.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


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
        String contentType = file.getContentType();
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        if (!isAllowedImage(contentType, extension)) {
            throw new IllegalArgumentException("Недопустимый тип файла. Разрешены только изображения (JPG, PNG, WEBP).");
        }

        String fileName = filenamePrefix + "." + extension;
        Path path = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(path);
            Path filePath = path.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return "/api/uploads/images" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить файл: " + fileName, e);
        }
    }

    private boolean isAllowedImage(String contentType, String extension) {
        if (contentType == null || extension == null) return false;

        // Проверка расширения (чтобы не загрузили .sh или .exe с типом image/png)
        String lowerExt = extension.toLowerCase();
        boolean validExtension = lowerExt.equals("jpg") || lowerExt.equals("jpeg") ||
                lowerExt.equals("png") || lowerExt.equals("webp");

        boolean validMime = contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/webp");

        return validMime && validExtension;
    }
}
