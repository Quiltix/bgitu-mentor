package com.bgitu.mentor.common.controller;

import com.bgitu.mentor.common.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import java.io.IOException;

@Tag(name = "Files", description = "Методы для взаимодействия с файлами")
@RestController
@RequestMapping("/api/uploads/image")
@RequiredArgsConstructor
public class FileController {

  private final FileStorageService fileStorageService;

  @Operation(
      summary = "Получение изображения",
      description = "Без авторизации. Пример пути: /api/uploads/image/avatars/uuid.png")
  @GetMapping("/{directory}/{filename:.+}")
  public ResponseEntity<Resource> serveImage(
      @PathVariable String directory, @PathVariable String filename) throws IOException {

    String relativePath = directory + File.pathSeparator + filename;
    Resource resource = fileStorageService.loadAsResource(relativePath);

    // Определяем Content-Type
    String contentType = Files.probeContentType(Path.of(resource.getURI()));
    if (contentType == null) {
      contentType = "application/octet-stream";
    }

    return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
  }
}
