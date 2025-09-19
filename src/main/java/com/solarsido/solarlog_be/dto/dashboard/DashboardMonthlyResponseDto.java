package com.solarsido.solarlog_be.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardMonthlyResponseDto {
  private String peakPowerDay;
  private float peakPower;
  private float totalMonthlyPower;
  private float co2Reduction;
  private float dayCompared; // 전월 대비 증감률
}