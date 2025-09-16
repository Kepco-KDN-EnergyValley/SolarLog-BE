package com.solarsido.solarlog_be.controller;

import com.solarsido.solarlog_be.dto.ApiResponseDto;
import com.solarsido.solarlog_be.auth.JwtTokenProvider;
import com.solarsido.solarlog_be.dto.dashboard.DashboardHourlyPowerResponseDto;
import com.solarsido.solarlog_be.dto.dashboard.DashboardTodayResponseDto;
import com.solarsido.solarlog_be.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

  private final DashboardService dashboardService;
  private final JwtTokenProvider jwtTokenProvider;

  // API 1: '오늘의 발전량' (요약 정보)
  @GetMapping("/today")
  public ResponseEntity<ApiResponseDto<DashboardTodayResponseDto>> getTodayPowerSummary(@RequestHeader("Authorization") String tokenHeader) {
    try {
      String accessToken = tokenHeader.substring(7);
      String userId = jwtTokenProvider.getUserId(accessToken);

      DashboardTodayResponseDto responseDto = dashboardService.getTodaySummary(userId);
      return new ResponseEntity<>(new ApiResponseDto<>(responseDto), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new ApiResponseDto<>(false, "유효하지 않은 토큰입니다."), HttpStatus.UNAUTHORIZED);
    }
  }

  // API 2: '오늘의 발전량 조회' (시간대별 데이터)
  @GetMapping("/today-inquiry")
  public ResponseEntity<ApiResponseDto<List<DashboardHourlyPowerResponseDto>>> getTodayPowerHourly(@RequestHeader("Authorization") String tokenHeader) {
    try {
      String accessToken = tokenHeader.substring(7);
      String userId = jwtTokenProvider.getUserId(accessToken);

      List<DashboardHourlyPowerResponseDto> responseDtoList = dashboardService.getHourlyPower(userId);
      return new ResponseEntity<>(new ApiResponseDto<>(responseDtoList), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new ApiResponseDto<>(false, "유효하지 않은 토큰입니다."), HttpStatus.UNAUTHORIZED);
    }
  }
}