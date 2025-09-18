package com.solarsido.solarlog_be.controller;

import com.solarsido.solarlog_be.dto.ApiResponseDto;
import com.solarsido.solarlog_be.auth.JwtTokenProvider;
import com.solarsido.solarlog_be.dto.dashboard.DashboardDailyResponseDto;
import com.solarsido.solarlog_be.dto.dashboard.DashboardHourlyPowerResponseDto;
import com.solarsido.solarlog_be.dto.dashboard.DashboardTodayResponseDto;
import com.solarsido.solarlog_be.service.DashboardService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

  private final DashboardService dashboardService;
  private final JwtTokenProvider jwtTokenProvider;

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

  @GetMapping("/daily")
  public ResponseEntity<ApiResponseDto<DashboardDailyResponseDto>> getDailyStats(
      @RequestHeader("Authorization") String tokenHeader,
      @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
  ) {
    try {
      String accessToken = tokenHeader.substring(7);
      String userId = jwtTokenProvider.getUserId(accessToken);

      DashboardDailyResponseDto responseDto = dashboardService.getDailyStats(userId, date);
      return new ResponseEntity<>(new ApiResponseDto<>(responseDto), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new ApiResponseDto<>(false, "유효하지 않은 토큰이거나 날짜 형식이 올바르지 않습니다."), HttpStatus.UNAUTHORIZED);
    }
  }

  @GetMapping("/daily-inquiry")
  public ResponseEntity<ApiResponseDto<List<DashboardHourlyPowerResponseDto>>> getDailyHourlyPower(
      @RequestHeader("Authorization") String tokenHeader,
      @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
  ) {
    try {
      String accessToken = tokenHeader.substring(7);
      String userId = jwtTokenProvider.getUserId(accessToken);

      List<DashboardHourlyPowerResponseDto> responseDtoList = dashboardService.getDailyHourlyPower(userId, date);
      return new ResponseEntity<>(new ApiResponseDto<>(responseDtoList), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(new ApiResponseDto<>(false, "유효하지 않은 토큰이거나 날짜 형식이 올바르지 않습니다."), HttpStatus.UNAUTHORIZED);
    }
  }
}