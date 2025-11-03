package com.solarsido.solarlog_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
public class DailyAverageDto {

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Asia/Seoul")
  @Column(nullable = false)
  private ZonedDateTime measuredDate;
  private Double avgPower;

  // JPA가 사용하는 생성자
  public DailyAverageDto(ZonedDateTime measuredDate, Double avgPower) {
    this.measuredDate = measuredDate;
    this.avgPower = avgPower;
  }

  // getter
  public ZonedDateTime getMeasuredDate() {
    return measuredDate;
  }

  public Double getAvgPower() {
    return avgPower;
  }
}

