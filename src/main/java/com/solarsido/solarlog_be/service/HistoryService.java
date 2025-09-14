package com.solarsido.solarlog_be.service;

import com.solarsido.solarlog_be.dto.HistoryResponseDto;
import com.solarsido.solarlog_be.entity.Alarm;
import com.solarsido.solarlog_be.entity.User;
import com.solarsido.solarlog_be.repository.HistoryRepository;
import com.solarsido.solarlog_be.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

  private final HistoryRepository historyRepository;
  private final UserRepository userRepository;

  public HistoryService(HistoryRepository historyRepository, UserRepository userRepository) {
    this.historyRepository = historyRepository;
    this.userRepository = userRepository;
  }

  public List<HistoryResponseDto.AlarmList> getAllHistory(String userId) {
    User user = userRepository.findByUserId(userId).orElseThrow(()->new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    List<Alarm> historyList = historyRepository.findAllBySolarPanel_UserOrderByAlarmDateDesc(user);

    return historyList.stream().map(alarm -> new HistoryResponseDto.AlarmList(
        alarm.getAlarmId(),
        alarm.getAlarmDate(),
        alarm.getSolarPanel().getModelName(),
        alarm.getAiDetectionEvent().getEventType(),
        alarm.getAiDetectionEvent().getEventDetail(),
        alarm.isRead()
    )).toList();

  }


}
