package com.solarsido.solarlog_be.service;

import com.solarsido.solarlog_be.dto.mypage.MyPageInstallationResponseDto;
import com.solarsido.solarlog_be.dto.mypage.MyPagePanelResponseDto;
import com.solarsido.solarlog_be.entity.SolarPanel;
import com.solarsido.solarlog_be.entity.User;
import com.solarsido.solarlog_be.repository.PanelDataRepository;
import com.solarsido.solarlog_be.repository.SolarPanelRepository;
import com.solarsido.solarlog_be.repository.UserRepository;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

  private final UserRepository userRepository;
  private final SolarPanelRepository solarPanelRepository;
  private final PanelDataRepository panelDataRepository;

  public MyPagePanelResponseDto getMyPagePanelInfo(String userId) {
    // 1. 유저와 패널 조회
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    SolarPanel solarPanel = solarPanelRepository.findByUser(user)
        .orElseThrow(() -> new IllegalArgumentException("패널 정보를 찾을 수 없습니다."));

    // 2. 설치 첫 달 발전량 (baseline)
    YearMonth installMonth = YearMonth.from(solarPanel.getInstallDate());
    Double baseline = panelDataRepository.findMonthlyTotalPower(
        solarPanel.getPanelId(),
        installMonth.getYear(),
        installMonth.getMonthValue()
    );
    if (baseline == null) baseline = 0.0;

    // 3. 최근 달 발전량
    YearMonth thisMonth = YearMonth.now();
    Double current = panelDataRepository.findMonthlyTotalPower(
        solarPanel.getPanelId(),
        thisMonth.getYear(),
        thisMonth.getMonthValue()
    );
    if (current == null) current = 0.0;

    // 4. 성능 계산
    float performance = 0.0f;
    if (baseline > 0) {
      performance = (float) ((current / baseline) * 100.0);  // 퍼센트 값
    }

    // 5. DTO 반환
    return new MyPagePanelResponseDto(
        solarPanel.getModelName(),
        solarPanel.getMaker(),
        solarPanel.getSerialNum(),
        performance,
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
