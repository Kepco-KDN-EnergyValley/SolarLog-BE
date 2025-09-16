package com.solarsido.solarlog_be.service;

import com.solarsido.solarlog_be.dto.dashboard.DashboardHourlyPowerResponseDto;
import com.solarsido.solarlog_be.dto.dashboard.DashboardTodayResponseDto;
import com.solarsido.solarlog_be.entity.PanelData;
import com.solarsido.solarlog_be.entity.SolarPanel;
import com.solarsido.solarlog_be.entity.User;
import com.solarsido.solarlog_be.repository.PanelDataRepository;
import com.solarsido.solarlog_be.repository.SolarPanelRepository;
import com.solarsido.solarlog_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class DashboardService {

  private final UserRepository userRepository;
  private final SolarPanelRepository solarPanelRepository;
  private final PanelDataRepository panelDataRepository;

  // API 1: '오늘의 발전량' (요약 정보)
  public DashboardTodayResponseDto getTodaySummary(String userId) {
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    SolarPanel solarPanel = solarPanelRepository.findByUser(user)
        .orElseThrow(() -> new IllegalArgumentException("패널 정보를 찾을 수 없습니다."));

    LocalDateTime todayStart = LocalDate.now().atStartOfDay();
    LocalDateTime todayEnd = LocalDateTime.now();

    List<PanelData> todayData = panelDataRepository.findAllBySolarPanelAndMeasuredDateBetween(solarPanel, todayStart, todayEnd);
    List<PanelData> allData = panelDataRepository.findAllBySolarPanel(solarPanel);

    float cumulativePower = (float) allData.stream()
        .mapToDouble(PanelData::getPower)
        .sum();
    float totalDailyPower = (float) todayData.stream()
        .mapToDouble(PanelData::getPower)
        .sum();
    float currentPower = (float) todayData.stream()
        .mapToDouble(PanelData::getPower)
        .max()
        .orElse(0.0);

    return new DashboardTodayResponseDto(cumulativePower, currentPower, totalDailyPower);
  }

  // API 2: '오늘의 발전량 조회' (시간대별 데이터)
  public List<DashboardHourlyPowerResponseDto> getHourlyPower(String userId) {
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    SolarPanel solarPanel = solarPanelRepository.findByUser(user)
        .orElseThrow(() -> new IllegalArgumentException("패널 정보를 찾을 수 없습니다."));

    LocalDateTime todayStart = LocalDate.now().atStartOfDay();
    LocalDateTime todayEnd = LocalDateTime.now();

    LocalDateTime timeRangeStart = todayStart.withHour(5);
    int endHour = todayEnd.getHour();

    List<PanelData> dailyData = panelDataRepository.findAllBySolarPanelAndMeasuredDateBetween(solarPanel, timeRangeStart, todayEnd);

    Map<Integer, Double> hourlyPower = dailyData.stream()
        .collect(Collectors.groupingBy(
            data -> data.getMeasuredDate().getHour(),
            Collectors.summingDouble(PanelData::getPower)
        ));

    return IntStream.rangeClosed(5, endHour)
        .mapToObj(hour -> {
          // getOrDefault()의 반환 값이 Double 객체이므로 floatValue() 메소드를 사용
          float power = hourlyPower.getOrDefault(hour, 0.0).floatValue();
          LocalDateTime hourTime = todayStart.withHour(hour);
          return new DashboardHourlyPowerResponseDto(hourTime, power);
        })
        .collect(Collectors.toList());
  }
}