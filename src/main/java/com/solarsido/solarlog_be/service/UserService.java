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

  @Transactional // 이 메소드 내부의 작업들이 하나의 트랜잭션으로 처리됨을 보장
  public void join(UserJoinRequestDto requestDto) {
    // 1. 아이디 중복 확인
    if (memberRepository.existsByUserId(requestDto.getUserId())) {
      throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
    }

    // 2. DTO를 Entity로 변환 (비밀번호 암호화 로직 추가)
    String encodedPassword = passwordEncoder.encode(requestDto.getPassword()); // 비밀번호 암호화
    User member = new User(requestDto.getUserId(), encodedPassword);

    // 3. Repository를 통해 DB에 저장
    memberRepository.save(member);
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
