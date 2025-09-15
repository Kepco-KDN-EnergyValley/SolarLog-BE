package com.solarsido.solarlog_be.service;

import com.solarsido.solarlog_be.dto.IsReadDto;
import com.solarsido.solarlog_be.entity.Alarm;
import com.solarsido.solarlog_be.repository.AlarmRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlarmService {

  private final AlarmRepository alarmRepository;
  public AlarmService(AlarmRepository alarmRepository) {
    this.alarmRepository = alarmRepository;
  }

  @Transactional
  public IsReadDto IsRead(Long alarmId){
    Alarm alarm = alarmRepository.findById(alarmId)
        .orElseThrow(()->new IllegalArgumentException("알람을 찾을 수 업습니다."));

    alarm.ChangeIsRead();

    return new IsReadDto(true);
  }

}
