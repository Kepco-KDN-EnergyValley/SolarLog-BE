package com.solarsido.solarlog_be.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DailyAverageDto {

  private LocalDate date;
  private Double avgPower;

  public DailyAverageDto(java.sql.Date sqlDate, Double avgPower) {
    this.date = sqlDate.toLocalDate(); // LocalDate 변환
    this.avgPower = avgPower;
  }
}
