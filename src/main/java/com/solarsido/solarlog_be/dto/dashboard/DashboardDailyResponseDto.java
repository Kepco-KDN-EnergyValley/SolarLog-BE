package com.solarsido.solarlog_be.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardDailyResponseDto {
  private int peakPowerTime;
  private float peakPower;
  private float totalDailyPower;
  private float co2Reduction;
  private float dayCompared;
}