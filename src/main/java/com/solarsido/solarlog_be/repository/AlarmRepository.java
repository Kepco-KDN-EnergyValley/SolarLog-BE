package com.solarsido.solarlog_be.repository;

import com.solarsido.solarlog_be.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
