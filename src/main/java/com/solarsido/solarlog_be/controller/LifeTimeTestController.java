package com.solarsido.solarlog_be.controller;

import com.solarsido.solarlog_be.scheduler.LifeTimeScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test")
public class LifeTimeTestController {

  private final LifeTimeScheduler lifeTimeScheduler;

  @PostMapping("/lifetime")
  public String testLifetimeCalculation() {
    lifeTimeScheduler.calculateLifeTime(); // 스케줄러 메서드 직접 호출
    return "수명 계산됨";
  }
}
