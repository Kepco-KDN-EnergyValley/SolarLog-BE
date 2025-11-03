package com.solarsido.solarlog_be.dto.dashboard;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class DashboardHourlyPowerResponseDto {
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Asia/Seoul")
  @Column(nullable = false)
  private ZonedDateTime measuredDate;
  private float power;
}