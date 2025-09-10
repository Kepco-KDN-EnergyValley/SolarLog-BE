package com.solarsido.solarlog_be.repository;

import com.solarsido.solarlog_be.entity.PanelRoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PanelRoiRepository extends JpaRepository<PanelRoi, Long> {

}
