package com.solarsido.solarlog_be.repository;

import com.solarsido.solarlog_be.entity.Alarm;
import com.solarsido.solarlog_be.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<Alarm, Long> {
  List<Alarm> findAllBySolarPanel_UserOrderByAlarmDateDesc(User user);

}
