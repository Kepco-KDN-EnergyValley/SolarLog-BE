package com.solarsido.solarlog_be.service;

import com.solarsido.solarlog_be.auth.JwtTokenDto;
import com.solarsido.solarlog_be.auth.JwtTokenProvider;
import com.solarsido.solarlog_be.dto.member.CheckIdRequestDto;
import com.solarsido.solarlog_be.dto.member.UserJoinRequestDto;
import com.solarsido.solarlog_be.dto.member.LoginRequestDto;
import com.solarsido.solarlog_be.entity.SolarPanel;
import com.solarsido.solarlog_be.entity.User;
import com.solarsido.solarlog_be.repository.SolarPanelRepository;
import com.solarsido.solarlog_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final SolarPanelRepository solarPanelRepository;

  @Transactional
  public void join(UserJoinRequestDto requestDto) {
    if (userRepository.existsByUserId(requestDto.getUserId())) {
      throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
    }

    String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
    User user = new User(requestDto.getUserId(), encodedPassword);
    userRepository.save(user);

    SolarPanel panel = new SolarPanel(
        null,
        requestDto.getModelName(),
        requestDto.getMaker(),
        requestDto.getSerialNum(),
        requestDto.getInstallDate(),
        requestDto.getInstallLocation(),
        0,
        0,
        requestDto.getInitialPower(),
        0,
        requestDto.getInitialPower(),
        user
    );
    solarPanelRepository.save(panel);
  }


  @Transactional(readOnly = true)
  public boolean checkUserIdDuplication(CheckIdRequestDto requestDto) {
    return userRepository.existsByUserId(requestDto.getUserId());
  }

  public JwtTokenDto login(LoginRequestDto requestDto) {
    User user = userRepository.findByUserId(requestDto.getUserId())
        .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

    if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
      throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
    }

    Optional<SolarPanel> solarPanelOptional = solarPanelRepository.findByUser(user);
    if (!solarPanelOptional.isPresent()) {
      throw new RuntimeException("패널 정보가 없습니다.");
    }

    String installLocation = solarPanelOptional.get().getInstallLocation();

    String accessToken = jwtTokenProvider.createToken(user.getUserId(), installLocation);

    return new JwtTokenDto(accessToken, installLocation);
  }
}