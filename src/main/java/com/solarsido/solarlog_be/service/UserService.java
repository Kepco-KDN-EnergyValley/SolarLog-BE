package com.solarsido.solarlog_be.service;

import com.solarsido.solarlog_be.auth.JwtTokenDto;
import com.solarsido.solarlog_be.dto.member.CheckIdRequestDto;
import com.solarsido.solarlog_be.dto.member.UserJoinRequestDto;
import com.solarsido.solarlog_be.entity.User;
import com.solarsido.solarlog_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.solarsido.solarlog_be.dto.member.LoginRequestDto;
import com.solarsido.solarlog_be.entity.SolarPanel;
import com.solarsido.solarlog_be.repository.SolarPanelRepository;


//JWT 추가
import com.solarsido.solarlog_be.auth.JwtTokenProvider;
//비밀번호 암호화
import org.springframework.security.crypto.password.PasswordEncoder;

@Service // 이 클래스가 Service 컴포넌트임을 나타냅니다.
@RequiredArgsConstructor // final 필드를 매개변수로 받는 생성자를 자동 생성
public class UserService {

  private final UserRepository memberRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final SolarPanelRepository solarPanelRepository;

  @Transactional
  public void join(UserJoinRequestDto requestDto) {
    // 1. 아이디 중복 확인
    if (memberRepository.existsByUserId(requestDto.getUserId())) {
      throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
    }

    // 2. 유저 저장
    String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
    User member = new User(requestDto.getUserId(), encodedPassword);
    memberRepository.save(member);

    // 3. 패널 저장 (요청 DTO에서 가져온 값으로 생성)
    SolarPanel panel = new SolarPanel(
        null, // PK 자동 생성
        requestDto.getModelName(),
        requestDto.getMaker(),
        requestDto.getSerialNum(),
        requestDto.getInstallDate(),
        requestDto.getInstallLocation(),
        0,                           // pollutionCount 초기값
        0,                           // faultCount 초기값
        requestDto.getInitialPower(),// initialPower
        100,                         // leftLife (임의 기본값, 계산 로직 필요하면 수정)
        requestDto.getInitialPower(),// capability (여기선 initialPower를 그대로 capability로 저장 예시)
        member                       // User 매핑
    );
    solarPanelRepository.save(panel);
  }


  @Transactional(readOnly = true) // 데이터 변경 없이 조회만 할 때 사용합니다.
  public boolean checkUserIdDuplication(CheckIdRequestDto requestDto) {
    return memberRepository.existsByUserId(requestDto.getUserId());
  }

  // 로그인 메소드 추가
  public JwtTokenDto login(LoginRequestDto requestDto) {
    User member = memberRepository.findByUserId(requestDto.getUserId())
        .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

    // 비밀번호 일치 여부 확인 (BCryptPasswordEncoder의 matches 메소드 사용)
    if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
      throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
    }

    String accessToken = jwtTokenProvider.createToken(member.getUserId());
    return new JwtTokenDto(accessToken);
  }
}
