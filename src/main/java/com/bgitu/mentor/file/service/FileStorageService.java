package com.bgitu.mentor.file.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

  /**
   * Сохраняет файл и возвращает его уникальное, сгенерированное имя.
   *
   * @param file Файл для сохранения
   * @param subDirectory Поддиректория для организации (e.g., "avatars", "articles")
   * @return Уникальное имя сохраненного файла (включая расширение)
   */
  String store(MultipartFile file, String subDirectory);

  /**
   * Загружает файл как ресурс по его имени.
   *
   * @param filename Имя файла
   * @return Ресурс для отправки клиенту
   */
  Resource loadAsResource(String filename);
}
