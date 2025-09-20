package com.solarsido.solarlog_be.service;

import com.solarsido.solarlog_be.dto.dashboard.DashboardDailyResponseDto;
import com.solarsido.solarlog_be.dto.dashboard.DashboardHourlyPowerResponseDto;
import com.solarsido.solarlog_be.dto.dashboard.DashboardMonthlyResponseDto;
import com.solarsido.solarlog_be.dto.dashboard.DashboardTodayResponseDto;
import com.solarsido.solarlog_be.dto.dashboard.DashboardMonthlyPowerResponseDto;
import com.solarsido.solarlog_be.entity.PanelData;
import com.solarsido.solarlog_be.entity.SolarPanel;
import com.solarsido.solarlog_be.entity.User;
import com.solarsido.solarlog_be.repository.PanelDataRepository;
import com.solarsido.solarlog_be.repository.SolarPanelRepository;
import com.solarsido.solarlog_be.repository.UserRepository;

import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

  private static final float CO2_EMISSION_FACTOR = 0.43f;
  // Co2 절감량 계산에 필요한 1kWh의 전기를 생산했을 때 절약되는 CO2의 양 (배출계수)

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

  // API 3: '일별 통계' (날짜별 요약 정보)
  public DashboardDailyResponseDto getDailyStats(String userId, LocalDate date) {
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    SolarPanel solarPanel = solarPanelRepository.findByUser(user)
        .orElseThrow(() -> new IllegalArgumentException("패널 정보를 찾을 수 없습니다."));

    LocalDateTime dayStart = date.atStartOfDay();
    LocalDateTime dayEnd = dayStart.plusDays(1).minusNanos(1);

    List<PanelData> selectedDayData = panelDataRepository.findAllBySolarPanelAndMeasuredDateBetween(solarPanel, dayStart, dayEnd);

    if (selectedDayData.isEmpty()) {
      return new DashboardDailyResponseDto(0, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    PanelData peakPowerData = selectedDayData.stream()
        .max(Comparator.comparing(PanelData::getPower))
        .orElse(null);
    int peakPowerTime = peakPowerData.getMeasuredDate().getHour();
    float peakPower = peakPowerData.getPower();

    float totalDailyPower = (float) selectedDayData.stream()
        .mapToDouble(PanelData::getPower)
        .sum();

    float co2Reduction = totalDailyPower * CO2_EMISSION_FACTOR;

    LocalDate yesterday = date.minusDays(1);
    LocalDateTime yesterdayStart = yesterday.atStartOfDay();
    LocalDateTime yesterdayEnd = yesterdayStart.plusDays(1).minusNanos(1);

    List<PanelData> yesterdayData = panelDataRepository.findAllBySolarPanelAndMeasuredDateBetween(solarPanel, yesterdayStart, yesterdayEnd);
    float totalYesterdayPower = (float) yesterdayData.stream()
        .mapToDouble(PanelData::getPower)
        .sum();

    float dayCompared = 0.0f;
    if (totalYesterdayPower > 0) {
      dayCompared = ((totalDailyPower - totalYesterdayPower) / totalYesterdayPower) * 100.0f;
    }

    return new DashboardDailyResponseDto(peakPowerTime, peakPower, totalDailyPower, co2Reduction, dayCompared);
  }

  // API 4: '일별 한시간단위 발전량 조회' (특정 날짜 기준)
  public List<DashboardHourlyPowerResponseDto> getDailyHourlyPower(String userId, LocalDate date) {
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    SolarPanel solarPanel = solarPanelRepository.findByUser(user)
        .orElseThrow(() -> new IllegalArgumentException("패널 정보를 찾을 수 없습니다."));

    LocalDateTime dayStart = date.atStartOfDay().withHour(5);
    LocalDateTime dayEnd = date.atTime(LocalTime.of(21, 59, 59, 999999999));

    List<PanelData> dailyData = panelDataRepository.findAllBySolarPanelAndMeasuredDateBetween(solarPanel, dayStart, dayEnd);

    Map<Integer, Double> hourlyPower = dailyData.stream()
        .collect(Collectors.groupingBy(
            data -> data.getMeasuredDate().getHour(),
            Collectors.summingDouble(PanelData::getPower)
        ));

    return IntStream.rangeClosed(5, 21)
        .mapToObj(hour -> {
          float power = hourlyPower.getOrDefault(hour, 0.0).floatValue();
          LocalDateTime hourTime = dayStart.withHour(hour);
          return new DashboardHourlyPowerResponseDto(hourTime, power);
        })
        .collect(Collectors.toList());
  }

  // API 5: '월별 통계' (월별 요약 정보)
  public DashboardMonthlyResponseDto getMonthlyStats(String userId, int year, int month) {
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    SolarPanel solarPanel = solarPanelRepository.findByUser(user)
        .orElseThrow(() -> new IllegalArgumentException("패널 정보를 찾을 수 없습니다."));

    LocalDateTime monthStart = LocalDate.of(year, month, 1).atStartOfDay();
    LocalDateTime monthEnd = monthStart.plusMonths(1).minusNanos(1);

    // 선택된 월의 데이터 조회
    List<PanelData> selectedMonthData = panelDataRepository.findAllBySolarPanelAndMeasuredDateBetween(solarPanel, monthStart, monthEnd);

    if (selectedMonthData.isEmpty()) {
      return new DashboardMonthlyResponseDto(0, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    // 최고 출력량 및 일자 계산
    Map<LocalDate, Double> dailyPower = selectedMonthData.stream()
        .collect(Collectors.groupingBy(
            data -> data.getMeasuredDate().toLocalDate(),
            Collectors.summingDouble(PanelData::getPower)
        ));

    Optional<Entry<LocalDate, Double>> peakPowerEntry = dailyPower.entrySet().stream()
        .max(Comparator.comparing(Map.Entry::getValue));

    int peakPowerDay = peakPowerEntry.map(entry -> entry.getKey().getDayOfMonth()).orElse(0);
    float peakPower = peakPowerEntry.map(entry -> (float) entry.getValue().doubleValue()).orElse(0.0f);

    // 총 발전량 계산
    float totalMonthlyPower = (float) selectedMonthData.stream()
        .mapToDouble(PanelData::getPower)
        .sum();

    // CO2 절감량 계산
    float co2Reduction = totalMonthlyPower * CO2_EMISSION_FACTOR;

    // 전월 대비 증감률 계산
    LocalDateTime prevMonthStart = monthStart.minusMonths(1);
    LocalDateTime prevMonthEnd = monthStart.minusNanos(1);
    List<PanelData> prevMonthData = panelDataRepository.findAllBySolarPanelAndMeasuredDateBetween(solarPanel, prevMonthStart, prevMonthEnd);
    float totalPrevMonthPower = (float) prevMonthData.stream()
        .mapToDouble(PanelData::getPower)
        .sum();

    float dayCompared = 0.0f;
    if (totalPrevMonthPower > 0) {
      dayCompared = ((totalMonthlyPower - totalPrevMonthPower) / totalPrevMonthPower) * 100.0f;
    }

    return new DashboardMonthlyResponseDto(peakPowerDay, peakPower, totalMonthlyPower, co2Reduction, dayCompared);
  }

  // API 6: '월별 발전량 조회' (주간 단위)
  public List<DashboardMonthlyPowerResponseDto> getMonthlyInquiry(String userId, int year, int month) {
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    SolarPanel solarPanel = solarPanelRepository.findByUser(user)
        .orElseThrow(() -> new IllegalArgumentException("패널 정보를 찾을 수 없습니다."));

    LocalDate monthStart = LocalDate.of(year, month, 1);
    LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);

    List<PanelData> monthlyData = panelDataRepository.findAllBySolarPanelAndMeasuredDateBetween(solarPanel, monthStart.atStartOfDay(), monthEnd.atTime(23, 59, 59, 999999999));

    // 주차별로 데이터를 그룹화하고 발전량을 합산
    Map<Integer, Double> weeklyPower = monthlyData.stream()
        .collect(Collectors.groupingBy(
            data -> data.getMeasuredDate().get(WeekFields.of(Locale.getDefault()).weekOfMonth()),
            Collectors.summingDouble(PanelData::getPower)
        ));

    List<DashboardMonthlyPowerResponseDto> responseList = new ArrayList<>();
    int daysInMonth = monthEnd.getDayOfMonth();

    for (int i = 1; i <= daysInMonth; i += 7) {
      int startDay = i;
      int endDay = Math.min(i + 6, daysInMonth);
      String period = String.format("%d일~%d일", startDay, endDay);

      // 해당 기간의 발전량 계산
      double powerSum = monthlyData.stream()
          .filter(data -> data.getMeasuredDate().getDayOfMonth() >= startDay && data.getMeasuredDate().getDayOfMonth() <= endDay)
          .mapToDouble(PanelData::getPower)
          .sum();

      responseList.add(new DashboardMonthlyPowerResponseDto(period, (float) powerSum));
    }

    return responseList;
  }
}