package com.solarsido.solarlog_be.repository;

import com.solarsido.solarlog_be.entity.SolarPanel;
import com.solarsido.solarlog_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // 이 import 문을 추가해야 합니다.

@Repository
public interface SolarPanelRepository extends JpaRepository<SolarPanel, Long> {

  List<SolarPanel> findAllByUser(User user);

  Optional<SolarPanel> findByUser(User user);
}