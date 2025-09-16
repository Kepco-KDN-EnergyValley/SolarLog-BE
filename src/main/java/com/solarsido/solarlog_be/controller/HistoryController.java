package com.solarsido.solarlog_be.controller;

import com.solarsido.solarlog_be.auth.JwtTokenProvider;
import com.solarsido.solarlog_be.dto.HistoryDetailResponseDto;
import com.solarsido.solarlog_be.dto.HistoryResponseDto;
import com.solarsido.solarlog_be.service.HistoryService;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/history")
public class HistoryController {

  private final HistoryService historyService;
  private final JwtTokenProvider jwtTokenProvider;
  public HistoryController(HistoryService historyService, JwtTokenProvider jwtTokenProvider) {
    this.historyService = historyService;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  // 히스토리 조회
  @GetMapping("")
  public ResponseEntity<?> getHistory(@RequestHeader("Authorization") String authHeader) {
    // 헤더에서 Bearer 제거
    String jwtToken = authHeader.replace("Bearer ","");
    // 헤더에서 user.id 추출
    String userId = jwtTokenProvider.getUserId(jwtToken);
    // 히스토리 추출
    List<HistoryResponseDto> histories = historyService.getAllHistory(userId);

    return ResponseEntity.ok(Map.of(
        "success",true,
        "data",histories
    ));
  }

  // 히스토리 상세 조회
  @GetMapping("/details/{alarmId}")
  public ResponseEntity<?> getHistoryDetail(@PathVariable long alarmId) {
    HistoryDetailResponseDto historyDetailResponseDto = historyService.getHistoryDetail(alarmId);

    return ResponseEntity.ok(Map.of(
        "success", true,
        "data", historyDetailResponseDto
    ));

  }

}
