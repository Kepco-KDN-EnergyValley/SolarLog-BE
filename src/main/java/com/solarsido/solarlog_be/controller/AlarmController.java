package com.solarsido.solarlog_be.controller;

import com.solarsido.solarlog_be.service.AlarmService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/alarm")
public class AlarmController {

  private final AlarmService alarmService;
  public AlarmController(AlarmService alarmService) {
    this.alarmService = alarmService;
  }

  @PostMapping("/read")
  public ResponseEntity<?> IsRead(@RequestParam Long alarmId) {
    return ResponseEntity.ok(Map.of(
        "success", true,
        "data", alarmService.IsRead(alarmId)
    ));
  }

}
