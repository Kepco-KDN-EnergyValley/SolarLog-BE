package com.solarsido.solarlog_be.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DashboardHourlyPowerResponseDto {
  @Column(nullable = false)
  private LocalDateTime measuredDate;
  private float power;
}