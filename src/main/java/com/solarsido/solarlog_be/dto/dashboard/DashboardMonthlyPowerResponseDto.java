package com.solarsido.solarlog_be.dto.dashboard;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DashboardMonthlyPowerResponseDto {
  private String period; // "1to7" 또는 "1일~7일"
  private float power;
}
