package com.solarsido.solarlog_be.service;

import com.solarsido.solarlog_be.dto.mypage.MyPagePanelResponseDto;
import com.solarsido.solarlog_be.entity.PanelData;
import com.solarsido.solarlog_be.entity.SolarPanel;
import com.solarsido.solarlog_be.entity.User;
import com.solarsido.solarlog_be.repository.PanelDataRepository;
import com.solarsido.solarlog_be.repository.SolarPanelRepository;
import com.solarsido.solarlog_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.solarsido.solarlog_be.dto.mypage.MyPageInstallationResponseDto;
import java.time.format.DateTimeFormatter;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {

  private final UserRepository userRepository;
  private final SolarPanelRepository solarPanelRepository;
  private final PanelDataRepository panelDataRepository;

  public MyPagePanelResponseDto getMyPagePanelInfo(String userId) {
    // 1. 유저 정보 조회
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    // 2. 유저의 패널 정보 조회 (하나의 패널만 있다고 가정)
    SolarPanel solarPanel = solarPanelRepository.findByUser(user)
        .orElseThrow(() -> new IllegalArgumentException("패널 정보를 찾을 수 없습니다."));

    // 3. 정격 출력 (고정 0.05 kW = 50W)
    float pRated = 0.05f;

    List<PanelData> allData = panelDataRepository.findAllBySolarPanel(solarPanel);

    // 실제 발전량 합계 (Eactual, kWh 단위라고 가정)
    double eActual = allData.stream()
        .mapToDouble(PanelData::getPower)
        .sum();

    // 정격 대비 발전량 (Specific Yield)
    float calculatedCapability = 0.0f;
    if (pRated > 0) {
      calculatedCapability = (float) (eActual / pRated);
    }

    // 4. DTO로 변환하여 반환
    return new MyPagePanelResponseDto(
        solarPanel.getModelName(),
        solarPanel.getMaker(),
        solarPanel.getSerialNum(),
        calculatedCapability, // kWh/kWp 단위 지표
        (int) solarPanel.getLeftLife()
    );
  }

  // 설치 정보 조회 메소드
  public MyPageInstallationResponseDto getMyPageInstallationInfo(String userId) {
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    SolarPanel solarPanel = solarPanelRepository.findAllByUser(user)
        .stream()
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("패널 정보를 찾을 수 없습니다."));

    // 날짜 포맷을 YYYY-MM-DD 형식의 문자열로 변환
    String installDateString = solarPanel.getInstallDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    return new MyPageInstallationResponseDto(
        installDateString,
        solarPanel.getInstallLocation(),
        solarPanel.getInitialPower()
    );
  }
}