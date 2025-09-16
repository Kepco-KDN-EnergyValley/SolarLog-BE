package com.solarsido.solarlog_be.dto;

import com.solarsido.solarlog_be.entity.EventDetail;
import com.solarsido.solarlog_be.entity.EventType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryResponseDto {
    private Long alarmId;
    private LocalDateTime alarmDate;
    private String modelName;
    private EventType eventType;
    private EventDetail eventDetail;
    private boolean isRead;

}
