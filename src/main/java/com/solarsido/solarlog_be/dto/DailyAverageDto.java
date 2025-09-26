package com.solarsido.solarlog_be.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DailyAverageDto {

  private LocalDate date;
  private Double avgPower;

}
