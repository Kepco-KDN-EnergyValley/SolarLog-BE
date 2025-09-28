package com.solarsido.solarlog_be.service;

import com.solarsido.solarlog_be.entity.PanelData;
import com.solarsido.solarlog_be.entity.SolarPanel;
import com.solarsido.solarlog_be.repository.PanelDataRepository;
import com.solarsido.solarlog_be.repository.SolarPanelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PanelDataService {

  private final PanelDataRepository panelDataRepository;
  private final SolarPanelRepository solarPanelRepository;

  public PanelDataService(PanelDataRepository panelDataRepository, SolarPanelRepository solarPanelRepository) {
    this.panelDataRepository = panelDataRepository;
    this.solarPanelRepository = solarPanelRepository;
  }

  @Transactional
  public void saveData(PanelData panelData) {
    SolarPanel solarPanel = solarPanelRepository.findById(panelData.getSolarPanel().getPanelId())
        .orElseThrow(()-> new IllegalArgumentException("태양광 패널을 찾을 수 없습니다."));

    PanelData data = new PanelData();
    data.setSolarPanel(solarPanel);
    data.setMeasuredDate(panelData.getMeasuredDate());
    data.setVoltage(panelData.getVoltage());
    data.setCurrent(panelData.getCurrent());
    data.setPower(panelData.getPower());

    panelDataRepository.save(data);
  }

}
