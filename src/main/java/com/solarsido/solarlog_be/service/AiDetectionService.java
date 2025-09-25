package com.solarsido.solarlog_be.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
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
import com.solarsido.solarlog_be.repository.FcmTokenRepository;
import com.solarsido.solarlog_be.repository.PanelRoiRepository;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AiDetectionService {

  private final AiDetectionRepository aiDetectionRepository;
  private final PanelRoiRepository panelRoiRepository;
  private final AiDetectionEventRepository aiDetectionEventRepository;
  private final FcmTokenRepository fcmTokenRepository;
  private final FirebaseMessaging firebaseMessaging;

  public AiDetectionService(AiDetectionRepository aiDetectionRepository,
      PanelRoiRepository panelRoiRepository,
      AiDetectionEventRepository aiDetectionEventRepository,
      FcmTokenRepository fcmTokenRepository,
      FirebaseMessaging firebaseMessaging) {
    this.aiDetectionRepository = aiDetectionRepository;
    this.panelRoiRepository = panelRoiRepository;
    this.aiDetectionEventRepository = aiDetectionEventRepository;
    this.fcmTokenRepository = fcmTokenRepository;
    this.firebaseMessaging = firebaseMessaging;
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
    ZonedDateTime seoulTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));

    // 4. 알람 생성
    Alarm alarm = new Alarm();
    alarm.setSolarPanel(matchedPanel);
    alarm.setAiDetectionEvent(savedEvent);
    alarm.setAlarmDate(seoulTime.toLocalDateTime());
    alarm.setRead(false);

    Alarm savedAlarm = aiDetectionRepository.save(alarm);

    // 5. FCM 푸시 전송
    //String title = savedEvent.getEventType().getDescription();
    //String body = savedEvent.getEventDetail().getDescription();

    // 유저의 모든 FCM 토큰 조회 후 전송
    matchedPanel.getUser().getFcmTokens().forEach(fcm -> {
      try {
        Notification notification = Notification.builder()
            //.setTitle(title)
            //.setBody(body)
            .build();

        Message message = Message.builder()
            .setToken(fcm.getFcmToken())
            //.setNotification(notification)
            .putData("alarmId", String.valueOf(savedAlarm.getAlarmId()))
            .putData("modelName",String.valueOf(savedAlarm.getSolarPanel().getModelName()))
            .putData("eventType", savedEvent.getEventType().getDescription())
            .putData("eventDetail", savedEvent.getEventDetail().getDescription())
            .build();

        firebaseMessaging.send(message);

      } catch (FirebaseMessagingException e) {
        String errorCode = e.getErrorCode().toString();
        if ("registration-token-not-registered".equals(errorCode) || "invalid-argument".equals(errorCode)) {
          // 더 이상 쓸 수 없는 토큰 → DB에서 제거
          fcmTokenRepository.deleteByFcmToken(fcm.getFcmToken());
        }
      }
    });

    // 6. 반환 DTO
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

