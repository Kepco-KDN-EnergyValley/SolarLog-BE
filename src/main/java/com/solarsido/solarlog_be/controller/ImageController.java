package com.solarsido.solarlog_be.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
// ImageController.java
@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

  private static final String UPLOAD_DIR = "/home/ubuntu/solarlog_images/";

  @PostMapping("/upload")
  public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
    try {
      // 저장 경로 확인 및 생성
      File dir = new File(UPLOAD_DIR);
      if (!dir.exists()) {
        dir.mkdirs();
      }

      // 파일 저장
      String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
      Path savePath = Paths.get(UPLOAD_DIR, filename);
      file.transferTo(savePath.toFile());

      // 프론트에서 접근 가능한 URL 반환
      String imageUrl = "https://3.37.135.99/images/" + filename;
      return ResponseEntity.ok(imageUrl);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed: " + e.getMessage());
    }
  }
}

