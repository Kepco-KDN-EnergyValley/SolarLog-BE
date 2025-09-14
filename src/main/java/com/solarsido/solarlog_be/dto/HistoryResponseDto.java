package com.solarsido.solarlog_be.dto;

import com.solarsido.solarlog_be.entity.EventDetail;
import com.solarsido.solarlog_be.entity.EventType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryResponseDto {

  private boolean success;
  private List<AlarmList> alarmLists;

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class AlarmList {
    private Long alarmId;
    private LocalDateTime alarmDate;
    private String modelName;
    private EventType eventType;
    private EventDetail eventDetail;
    private boolean isRead;
  }

}
