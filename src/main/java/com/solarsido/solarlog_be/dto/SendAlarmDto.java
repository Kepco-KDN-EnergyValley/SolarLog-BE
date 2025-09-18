package com.solarsido.solarlog_be.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendAlarmDto {
  private Long alarmId;
  private String panelName;
  private String eventType;
  private String eventDetail;

}
