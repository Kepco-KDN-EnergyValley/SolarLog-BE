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
import com.solarsido.solarlog_be.dto.ApiResponseDto;

@RestController
@RequestMapping("/api/v1/mypage")
@RequiredArgsConstructor
public class MyPageController {

  private final MyPageService myPageService;
  private final JwtTokenProvider jwtTokenProvider;

// MyPageController.java 파일 중 패널 정보 조회 메소드

  @GetMapping("/panel")
  public ResponseEntity<ApiResponseDto<MyPagePanelResponseDto>> getMyPagePanelInfo(@RequestHeader("Authorization") String tokenHeader) {
    try {
      String accessToken = tokenHeader.substring(7);
      String userId = jwtTokenProvider.getUserId(accessToken);

      MyPagePanelResponseDto responseDto = myPageService.getMyPagePanelInfo(userId);
      return new ResponseEntity<>(new ApiResponseDto<>(responseDto), HttpStatus.OK);
    } catch (Exception e) {
      // 실패 시, ApiResponseDto로 감싸서 메시지를 반환하도록 수정
      return new ResponseEntity<>(new ApiResponseDto<>(false, "유효하지 않은 토큰입니다."), HttpStatus.UNAUTHORIZED);
    }
  }

  // MyPageController.java 파일 중 설치 정보 조회 메소드
  @GetMapping("/installation")
  public ResponseEntity<ApiResponseDto<MyPageInstallationResponseDto>> getMyPageInstallationInfo(@RequestHeader("Authorization") String tokenHeader) {
    try {
      String accessToken = tokenHeader.substring(7);
      String userId = jwtTokenProvider.getUserId(accessToken);

      MyPageInstallationResponseDto responseDto = myPageService.getMyPageInstallationInfo(userId);
      return new ResponseEntity<>(new ApiResponseDto<>(responseDto), HttpStatus.OK);
    } catch (Exception e) {
      // 실패 시, ApiResponseDto로 감싸서 메시지를 반환하도록 수정
      return new ResponseEntity<>(new ApiResponseDto<>(false, "유효하지 않은 토큰입니다."), HttpStatus.UNAUTHORIZED);
    }
  }
}