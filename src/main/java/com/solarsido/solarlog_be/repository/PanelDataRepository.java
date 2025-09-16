// PanelDataRepository.java

package com.solarsido.solarlog_be.repository;

import com.solarsido.solarlog_be.entity.PanelData;
import com.solarsido.solarlog_be.entity.SolarPanel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PanelDataRepository extends JpaRepository<PanelData, Long> {
  // 특정 패널의 특정 날짜 데이터만 조회
  List<PanelData> findAllBySolarPanelAndMeasuredDateBetween(SolarPanel solarPanel, LocalDateTime start, LocalDateTime end);

  // 특정 패널의 모든 데이터 조회
  List<PanelData> findAllBySolarPanel(SolarPanel solarPanel);
}