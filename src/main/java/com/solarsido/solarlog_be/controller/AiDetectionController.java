package com.solarsido.solarlog_be.controller;

import com.solarsido.solarlog_be.dto.AiDetectionDto;
import com.solarsido.solarlog_be.dto.SendAlarmDto;
import com.solarsido.solarlog_be.service.AiDetectionService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai/detections")
public class AiDetectionController {
  private final AiDetectionService aiDetectionService;
  public AiDetectionController(AiDetectionService aiDetectionService) {
    this.aiDetectionService = aiDetectionService;
  }

  @PostMapping("")
  public ResponseEntity<?> AiDetectionAndSendAlarm(@RequestBody AiDetectionDto aiDetectionDto){
    SendAlarmDto alarmDto = aiDetectionService.AidetectionAndSendAlarm(aiDetectionDto);

    return ResponseEntity.ok(Map.of(
        "success", true,
        "data",alarmDto
    ));
  }

}
