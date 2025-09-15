package com.solarsido.solarlog_be.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryDetailResponseDto {

  private String eventType;
  private String eventDetail;
  private LocalDateTime alarmDate;
  private String image;

}
