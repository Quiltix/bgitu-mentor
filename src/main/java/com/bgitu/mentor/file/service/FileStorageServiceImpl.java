package com.bgitu.mentor.file.service;

import com.bgitu.mentor.exception.custom.FileStorageException;
import com.bgitu.mentor.exception.custom.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
  private static final List<String> ALLOWED_IMAGE_TYPES =
      Arrays.asList("image/jpeg", "image/png", "image/gif", "image/webp");

  private static final List<String> ALLOWED_IMAGE_EXTENSIONS =
      Arrays.asList("jpg", "jpeg", "png", "gif", "webp");

  private final Path rootLocation;

  @Autowired
  public FileStorageServiceImpl(@Value("${file.upload-dir}") String uploadDir) {
    this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
    try {
      Files.createDirectories(this.rootLocation);
    } catch (IOException e) {
      throw new FileStorageException("Could not initialize storage location", e);
    }
  }

  @Override
  public String store(MultipartFile file, String subDirectory) {
    String originalFilename =
        StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

    if (file.isEmpty()) {
      throw new FileStorageException("Ошибка в сохранении пустого файла " + originalFilename);
    }
    if (originalFilename.contains("..")) {
      throw new FileStorageException(
          "Ошибка в сохранении файла с этим названием " + originalFilename);
    }
    validateImageFile(file);

    try {
      String extension = StringUtils.getFilenameExtension(originalFilename);
      String newFilename = UUID.randomUUID() + "." + extension;

      Path subDirLocation = this.rootLocation.resolve(subDirectory);
      Files.createDirectories(subDirLocation); // Создаем поддиректорию, если ее нет

      Path destinationFile = subDirLocation.resolve(newFilename).normalize();

      try (InputStream inputStream = file.getInputStream()) {
        Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
      }

      // Возвращаем путь относительно корневой папки uploads, включая поддиректорию
      return Paths.get(subDirectory, newFilename).toString();
    } catch (IOException e) {
      throw new FileStorageException("Failed to store file " + originalFilename, e);
    }
  }

  @Override
  public Resource loadAsResource(String filename) {
    try {
      Path file = rootLocation.resolve(filename).normalize();
      Resource resource = new UrlResource(file.toUri());
      if (resource.exists() && resource.isReadable()) {

        if (!file.startsWith(this.rootLocation)) {
          throw new SecurityException("Ошибка в получении файла из этой директории.");
        }
        return resource;
      } else {
        throw new ResourceNotFoundException("Невозможно прочитать файл: " + filename);
      }
    } catch (MalformedURLException e) {
      throw new ResourceNotFoundException("Невозможно прочитать файл: " + filename);
    }
  }

  @Override
  public void delete(String relativePath) {
    if (relativePath == null || relativePath.isBlank()) {
      return;
    }

    try {
      Path fileToDelete = rootLocation.resolve(relativePath).normalize();

      if (!fileToDelete.startsWith(this.rootLocation)) {
        log.warn("Попытка удаления файла за пределами разрешенной директории: {}", relativePath);
        return;
      }

      Files.deleteIfExists(fileToDelete);
      log.info("Файл успешно удален: {}", relativePath);

    } catch (IOException e) {
      log.error("Не удалось удалить файл: {}", relativePath, e);
    }
  }

  private void validateImageFile(MultipartFile file) {
    String contentType = file.getContentType();
    if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
      throw new FileStorageException(
          "Недопустимый тип файла. Разрешены только изображения: " + ALLOWED_IMAGE_TYPES);
    }

    String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
    if (extension == null || !ALLOWED_IMAGE_EXTENSIONS.contains(extension.toLowerCase())) {
      throw new FileStorageException(
          "Недопустимое расширение файла. Разрешены: " + ALLOWED_IMAGE_EXTENSIONS);
    }
  }
}
