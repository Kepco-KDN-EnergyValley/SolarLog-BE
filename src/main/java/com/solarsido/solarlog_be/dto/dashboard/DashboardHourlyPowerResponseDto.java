package com.solarsido.solarlog_be.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DashboardHourlyPowerResponseDto {
  private LocalDateTime measuredDate;
  private float power;
}