package com.bgitu.mentor.common.controller;

import com.bgitu.mentor.common.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Tag(name = "Files", description = "Методы для взаимодействия с файлами")
@RestController
@RequestMapping("/api/uploads/image")
@RequiredArgsConstructor
public class FileController {

    private final FileStorageService fileStorageService;

    @Operation(summary = "Получение изображения", description = "Без авторизации. Пример пути: /api/uploads/image/avatars/uuid.png")
    @GetMapping("/{directory}/{filename:.+}")
    public ResponseEntity<Resource> serveImage(
            @PathVariable String directory,
            @PathVariable String filename) throws IOException {

        String relativePath = directory + "/" + filename;
        Resource resource = fileStorageService.loadAsResource(relativePath);

        // Определяем Content-Type
        String contentType = Files.probeContentType(Path.of(resource.getURI()));
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}