package com.solarsido.solarlog_be.repository;

import com.solarsido.solarlog_be.entity.SolarPanel;
import com.solarsido.solarlog_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolarPanelRepository extends JpaRepository<SolarPanel, Long> {
  // 특정 유저의 모든 패널을 조회하는 메소드
  List<SolarPanel> findAllByUser(User user);
}
