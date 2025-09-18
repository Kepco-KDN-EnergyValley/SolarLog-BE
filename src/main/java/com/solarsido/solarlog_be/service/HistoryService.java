package com.solarsido.solarlog_be.service;

import com.solarsido.solarlog_be.dto.HistoryDetailResponseDto;
import com.solarsido.solarlog_be.dto.HistoryResponseDto;
import com.solarsido.solarlog_be.entity.AiDetectionEvent;
import com.solarsido.solarlog_be.entity.Alarm;
import com.solarsido.solarlog_be.entity.User;
import com.solarsido.solarlog_be.repository.AlarmRepository;
import com.solarsido.solarlog_be.repository.HistoryRepository;
import com.solarsido.solarlog_be.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

  private final HistoryRepository historyRepository;
  private final UserRepository userRepository;
  private final AlarmRepository alarmRepository;

  public HistoryService(HistoryRepository historyRepository, UserRepository userRepository, AlarmRepository alarmRepository) {
    this.historyRepository = historyRepository;
    this.userRepository = userRepository;
    this.alarmRepository = alarmRepository;
  }

  // 히스토리 전체 조회
  public List<HistoryResponseDto> getAllHistory(String userId) {
    User user = userRepository.findByUserId(userId).orElseThrow(()->new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    List<Alarm> historyList = historyRepository.findAllBySolarPanel_UserOrderByAlarmDateDesc(user);

    return historyList.stream().map(alarm -> new HistoryResponseDto(
        alarm.getAlarmId(),
        alarm.getAlarmDate(),
        alarm.getSolarPanel().getModelName(),
        alarm.getAiDetectionEvent().getEventType().getDescription(),
        alarm.getAiDetectionEvent().getEventDetail().getDescription(),
        alarm.isRead()
    )).toList();
  }

  //히스토리 상세 조회
  public HistoryDetailResponseDto getHistoryDetail(Long alarmId) {
    Alarm details = alarmRepository.findById(alarmId)
        .orElseThrow(()->new IllegalArgumentException("알림 내용을 찾을 수 없습니다."));
    AiDetectionEvent events = details.getAiDetectionEvent();

    return new HistoryDetailResponseDto(
        events.getEventType().getDescription(),
        events.getEventDetail().getDescription(),
        details.getAlarmDate(),
        events.getImage()
    );
  }


}
