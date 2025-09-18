package com.solarsido.solarlog_be.service;

import com.solarsido.solarlog_be.dto.AiDetectionDto;
import com.solarsido.solarlog_be.dto.SendAlarmDto;
import com.solarsido.solarlog_be.entity.AiDetectionEvent;
import com.solarsido.solarlog_be.entity.Alarm;
import com.solarsido.solarlog_be.entity.EventDetail;
import com.solarsido.solarlog_be.entity.EventType;
import com.solarsido.solarlog_be.entity.PanelRoi;
import com.solarsido.solarlog_be.entity.SolarPanel;
import com.solarsido.solarlog_be.repository.AiDetectionEventRepository;
import com.solarsido.solarlog_be.repository.AiDetectionRepository;
import com.solarsido.solarlog_be.repository.PanelRoiRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AiDetectionService {

  private final AiDetectionRepository aiDetectionRepository;
  private final PanelRoiRepository panelRoiRepository;
  private final AiDetectionEventRepository aiDetectionEventRepository;

  public AiDetectionService(AiDetectionRepository aiDetectionRepository,
      PanelRoiRepository panelRoiRepository, AiDetectionEventRepository aiDetectionEventRepository) {
    this.aiDetectionRepository = aiDetectionRepository;
    this.panelRoiRepository = panelRoiRepository;
    this.aiDetectionEventRepository = aiDetectionEventRepository;
  }

  @Transactional
  public SendAlarmDto AidetectionAndSendAlarm(AiDetectionDto dto) {
    // 1. ROI DB 조회
    List<PanelRoi> rois = panelRoiRepository.findAll();

    // 2. 좌표 비교 → 매칭 패널 찾기
    PanelRoi matchedRoi = findMatchedPanel(dto, rois);
    SolarPanel matchedPanel = matchedRoi.getSolarPanel();

    // 3. 탐지 이벤트 저장
    AiDetectionEvent detectionEvent = new AiDetectionEvent();
    detectionEvent.setEventType(EventType.valueOf(dto.getEventType().toUpperCase()));
    detectionEvent.setEventDetail(EventDetail.valueOf(dto.getEventDetail().toUpperCase()));
    detectionEvent.setBboxX(dto.getBboxX());
    detectionEvent.setBboxY(dto.getBboxY());
    detectionEvent.setBboxW(dto.getBboxW());
    detectionEvent.setBboxH(dto.getBboxH());
    detectionEvent.setSolarPanel(matchedPanel);
    detectionEvent.setPanelRoi(matchedRoi);
    detectionEvent.setImage(dto.getImage());

    AiDetectionEvent savedEvent = aiDetectionEventRepository.save(detectionEvent);

    // 4. 알람 생성
    Alarm alarm = new Alarm();
    alarm.setSolarPanel(matchedPanel);
    alarm.setAiDetectionEvent(savedEvent);
    alarm.setRead(false);

    Alarm savedAlarm = aiDetectionRepository.save(alarm);

    // 5. 반환 DTO
    return new SendAlarmDto(
        savedAlarm.getAlarmId(),
        matchedPanel.getModelName(),
        savedEvent.getEventType().getDescription(),
        savedEvent.getEventDetail().getDescription()
    );
  }

  private PanelRoi findMatchedPanel(AiDetectionDto dto, List<PanelRoi> rois) {
    float x1 = dto.getBboxX();
    float y1 = dto.getBboxY();
    float x2 = dto.getBboxX() + dto.getBboxW();
    float y2 = dto.getBboxY() + dto.getBboxH();

    return rois.stream()
        .filter(roi -> !(roi.getXEnd() < x1 || roi.getXStart() > x2 ||
            roi.getYEnd() < y1 || roi.getYStart() > y2))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("해당 패널을 찾을 수 없음"));
  }
}
