// PanelDataRepository.java

package com.solarsido.solarlog_be.repository;

import com.solarsido.solarlog_be.dto.DailyAverageDto;
import com.solarsido.solarlog_be.entity.PanelData;
import com.solarsido.solarlog_be.entity.SolarPanel;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PanelDataRepository extends JpaRepository<PanelData, Long> {
  // 특정 패널의 특정 날짜 데이터만 조회
  List<PanelData> findAllBySolarPanelAndMeasuredDateBetween(SolarPanel solarPanel, ZonedDateTime start, ZonedDateTime end);

  // 특정 패널의 모든 데이터 조회
  List<PanelData> findAllBySolarPanel(SolarPanel solarPanel);

  // 일별 평균 발전량 조회
  @Query("SELECT new com.solarsido.solarlog_be.dto.DailyAverageDto(" +
      "DATE(p.measuredDate), AVG(p.power)) " +
      "FROM PanelData p " +
      "WHERE p.solarPanel.panelId = :panelId AND p.measuredDate BETWEEN :start AND :end " +
      "GROUP BY FUNCTION('DATE', p.measuredDate) " +
      "ORDER BY FUNCTION('DATE', p.measuredDate)")
  List<DailyAverageDto> findDailyAverage(
      @Param("panelId") Long panelId,
      @Param("start") ZonedDateTime start,
      @Param("end") ZonedDateTime end);

  @Query("SELECT SUM(p.power) " +
      "FROM PanelData p " +
      "WHERE p.solarPanel.panelId = :panelId " +
      "AND YEAR(p.measuredDate) = :year " +
      "AND MONTH(p.measuredDate) = :month")
  Double findMonthlyTotalPower(@Param("panelId") Long panelId,
      @Param("year") int year,
      @Param("month") int month);
}