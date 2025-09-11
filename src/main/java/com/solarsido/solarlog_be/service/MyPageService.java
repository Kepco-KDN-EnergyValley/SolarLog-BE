package com.solarsido.solarlog_be.service;

import com.solarsido.solarlog_be.dto.mypage.MyPagePanelResponseDto;
import com.solarsido.solarlog_be.entity.SolarPanel;
import com.solarsido.solarlog_be.entity.User;
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

  public MyPagePanelResponseDto getMyPagePanelInfo(String userId) {
    // 1. 유저 정보 조회
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    // 2. 유저의 패널 정보 조회 (한 유저당 하나의 패널만 있다고 가정)
    // findAllByUser는 List를 반환하지만, 여기서는 첫 번째 패널만 가져옵니다.
    SolarPanel solarPanel = solarPanelRepository.findAllByUser(user)
        .stream()
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("패널 정보를 찾을 수 없습니다."));

    // 3. DTO로 변환하여 반환
    return new MyPagePanelResponseDto(
        solarPanel.getModelName(),
        solarPanel.getMaker(),
        solarPanel.getSerialNum(),
        solarPanel.getCapability(),
        solarPanel.getLeftLife() // double 타입을 int로 변환
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