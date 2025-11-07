package com.solarsido.solarlog_be.service;

import com.solarsido.solarlog_be.dto.DailyAverageDto;
import com.solarsido.solarlog_be.entity.SolarPanel;
import com.solarsido.solarlog_be.repository.PanelDataRepository;
import com.solarsido.solarlog_be.repository.SolarPanelRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class LifeTimeSerivce {

  private final PanelDataRepository panelDataRepository;
  private final SolarPanelRepository solarPanelRepository;

  public LifeTimeSerivce(PanelDataRepository panelDataRepository, SolarPanelRepository solarPanelRepository) {
    this.panelDataRepository = panelDataRepository;
    this.solarPanelRepository = solarPanelRepository;
  }

  //모든 패널 조회
  public List<SolarPanel> findAllPanels(){
    return solarPanelRepository.findAll();
  }

  //패널의 한 달간 일별 평균 발전량 조회
  public List<Map<String,Object>> getMonthlyHistory(Long panelId, YearMonth targetMonth){

    LocalDateTime start = targetMonth.atDay(1).atStartOfDay();
    LocalDateTime end = targetMonth.atEndOfMonth().atTime(LocalTime.MAX);
    List<DailyAverageDto> dailyAverages = panelDataRepository.findDailyAverage(panelId,start,end);

    List<Map<String,Object>> history = new ArrayList<>();
    for(DailyAverageDto dailyAverage: dailyAverages){
      Map<String,Object> map = new HashMap<>();
      map.put("date",dailyAverage.getDate());
      map.put("avgPower",dailyAverage.getAvgPower());
      history.add(map);
    }

    return history;
  }

  //잔연수명 db 업데이트
  public void updateLifeTime(Long panelId, Integer leftLife){
    SolarPanel panel = solarPanelRepository.findById(panelId).orElseThrow(()->new IllegalArgumentException("패널을 찾을 수 없습니다."));
    panel.setLeftLife(leftLife);
    solarPanelRepository.save(panel);
  }

}
