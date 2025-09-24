package com.bgitu.mentor.file.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

  String store(MultipartFile file, String subDirectory);

  Resource loadAsResource(String filename);

  void delete(String relativePath);
}
