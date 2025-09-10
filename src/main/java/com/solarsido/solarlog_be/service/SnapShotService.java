package com.solarsido.solarlog_be.service;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.stereotype.Service;

@Service
public class SnapShotService {
  // 사진 저장할 경로
  private static final String SAVE_DIR = "/Users/doyeon/Desktop/SolarLog_Python/";

  // 사진 저장 및 파이썬 파일 실행
  public String saveSnapshotAndRunPython(String imageUrl) {
    try {
      // 1. URL 객체 생성
      URL url = new URL(imageUrl);

      // 2. 저장 파일 경로 생성 (timestamp 이용)
      String fileName = "snapshot_" + System.currentTimeMillis() + ".jpg";
      String savedPath = SAVE_DIR + fileName;

      // 3. URL에서 이미지 다운로드 → 로컬 저장
      try (InputStream in = url.openStream()) {
        Files.copy(in, Paths.get(savedPath), StandardCopyOption.REPLACE_EXISTING);
      }

      // 4. Python 스크립트 실행
      ProcessBuilder pb = new ProcessBuilder(
          "python3",
          "/Users/doyeon/Desktop/SolarLog_Python/roi_registers.py",
          savedPath
      );
      pb.inheritIO(); // 파이썬 로그를 자바 콘솔로 출력
      Process process = pb.start();
      int exitCode = process.waitFor();

      return "저장 성공: " + savedPath + " (exitCode=" + exitCode + ")";

    } catch (MalformedURLException e) {
      return "잘못된 URL 형식: " + imageUrl;
    } catch (Exception e) {
      return "에러 발생: " + e.getMessage();
    }
  }
}
