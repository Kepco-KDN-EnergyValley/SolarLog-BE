package com.solarsido.solarlog_be.service;

import com.solarsido.solarlog_be.dto.PanelRoiRequestDto;
import com.solarsido.solarlog_be.entity.PanelRoi;
import com.solarsido.solarlog_be.entity.SolarPanel;
import com.solarsido.solarlog_be.repository.PanelRoiRepository;
import com.solarsido.solarlog_be.repository.SolarPanelRepository;
import org.springframework.stereotype.Service;

@Service
public class PanelRoiService {
  private final PanelRoiRepository panelRoiRepository;
  private final SolarPanelRepository solarPanelRepository;
  public PanelRoiService(PanelRoiRepository panelRoiRepository, SolarPanelRepository solarPanelRepository) {
    this.panelRoiRepository = panelRoiRepository;
    this.solarPanelRepository = solarPanelRepository;
  }

  // 처음 태양광 패널 좌표 저장
  public void saveRoi(PanelRoiRequestDto panelRoiRequestDto) {
    // 패널 id 찾기
    SolarPanel solarPanel = solarPanelRepository.findById(panelRoiRequestDto.getPanelId())
        .orElseThrow(()->new IllegalArgumentException("패널을 찾을 수 없습니다."));

    for(PanelRoiRequestDto.CoordinateDto dto : panelRoiRequestDto.getCoordinates()){
      PanelRoi roi = new PanelRoi();
      roi.setSolarPanel(solarPanel);
      roi.setShapeDataX(dto.getX());
      roi.setShapeDataY(dto.getY());
      roi.setShapeDataW(dto.getW());
      roi.setShapeDataH(dto.getH());
      panelRoiRepository.save(roi);
    }
  }

}
