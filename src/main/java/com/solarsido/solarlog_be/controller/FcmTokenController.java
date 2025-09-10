package com.solarsido.solarlog_be.controller;

import com.solarsido.solarlog_be.auth.JwtTokenProvider;
import com.solarsido.solarlog_be.dto.FcmRequestDto;
import com.solarsido.solarlog_be.service.FcmTokenService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/fcm")
public class FcmTokenController {

  private final FcmTokenService fcmTokenService;
  private final JwtTokenProvider jwtTokenProvider;

  public FcmTokenController(FcmTokenService fcmTokenService, JwtTokenProvider jwtTokenProvider) {
    this.fcmTokenService = fcmTokenService;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  // fcm 토큰 등록
  @PostMapping("/registers")
  public ResponseEntity<?> registerToken(@RequestHeader("Authorization") String authHeader, @RequestBody FcmRequestDto fcmToken) {
    // 헤더에서 Bearer 제거
    String jwtToken = authHeader.replace("Bearer ","");
    // 헤더에서 user.id 추출
    String userId = jwtTokenProvider.getUserId(jwtToken);
    // fcm 토큰 등록 service 호출
    fcmTokenService.registerToken(userId, fcmToken.getFcmToken());

    return ResponseEntity.ok(Map.of(
        "success", true,
        "message" , "FCM 토큰 등록 완료"
    ));
  }

}
