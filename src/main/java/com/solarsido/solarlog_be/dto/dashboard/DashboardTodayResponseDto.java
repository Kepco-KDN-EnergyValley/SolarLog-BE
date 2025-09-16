package com.solarsido.solarlog_be.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardTodayResponseDto {
  private float cumulativePower;
  private float power; // 현재 출력량
  private float totalDailyPower;
}