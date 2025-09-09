package com.solarsido.solarlog_be.controller;

import com.solarsido.solarlog_be.dto.mypage.MyPagePanelResponseDto;
import com.solarsido.solarlog_be.service.MyPageService;
import com.solarsido.solarlog_be.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.solarsido.solarlog_be.dto.mypage.MyPageInstallationResponseDto;

@RestController
@RequestMapping("/api/v1/mypage")
@RequiredArgsConstructor
public class MyPageController {

  private final MyPageService myPageService;
  private final JwtTokenProvider jwtTokenProvider;

  @GetMapping("/panel")
  public ResponseEntity<MyPagePanelResponseDto> getMyPagePanelInfo(@RequestHeader("Authorization") String tokenHeader) {
    try {
      // "Bearer " 접두사 제거
      String accessToken = tokenHeader.substring(7);

      // JWT 토큰에서 userId 추출
      String userId = jwtTokenProvider.getUserId(accessToken);

      MyPagePanelResponseDto responseDto = myPageService.getMyPagePanelInfo(userId);
      return new ResponseEntity<>(responseDto, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
  }

  @GetMapping("/installation") // 새로운 엔드포인트
  public ResponseEntity<MyPageInstallationResponseDto> getMyPageInstallationInfo(@RequestHeader("Authorization") String tokenHeader) {
    try {
      String accessToken = tokenHeader.substring(7);
      String userId = jwtTokenProvider.getUserId(accessToken);

      MyPageInstallationResponseDto responseDto = myPageService.getMyPageInstallationInfo(userId);
      return new ResponseEntity<>(responseDto, HttpStatus.OK);
    } catch (Exception e) {
      System.err.println("API 호출 중 예외 발생: " + e.getMessage());
      e.printStackTrace();
      return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
  }
}