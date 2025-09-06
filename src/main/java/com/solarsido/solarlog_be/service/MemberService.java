package com.solarsido.solarlog_be.service;

import com.solarsido.solarlog_be.dto.member.CheckIdRequestDto;
import com.solarsido.solarlog_be.dto.member.MemberJoinRequestDto;
import com.solarsido.solarlog_be.entity.Member;
import com.solarsido.solarlog_be.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.solarsido.solarlog_be.dto.member.LoginRequestDto;

@Service // 이 클래스가 Service 컴포넌트임을 나타냅니다.
@RequiredArgsConstructor // final 필드를 매개변수로 받는 생성자를 자동 생성합니다.
public class MemberService {

  private final MemberRepository memberRepository;

  @Transactional // 이 메소드 내부의 작업들이 하나의 트랜잭션으로 처리됨을 보장합니다.
  public void join(MemberJoinRequestDto requestDto) {
    // 1. 아이디 중복 확인
    if (memberRepository.existsByUserId(requestDto.getUserId())) {
      throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
    }

    // 2. DTO를 Entity로 변환 (비밀번호는 암호화 필요)
    Member member = new Member(requestDto.getUserId(), requestDto.getPassword());

    // 3. Repository를 통해 DB에 저장
    memberRepository.save(member);
  }

  @Transactional(readOnly = true) // 데이터 변경 없이 조회만 할 때 사용합니다.
  public boolean checkUserIdDuplication(CheckIdRequestDto requestDto) {
    return memberRepository.existsByUserId(requestDto.getUserId());
  }

  // 로그인 메소드 추가
  public String login(LoginRequestDto requestDto) {
    // 1. 아이디로 회원 찾기
    Member member = memberRepository.findByUserId(requestDto.getUserId())
        .orElseThrow(() -> new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다."));

    // 2. 비밀번호 일치 여부 확인
    // 경고: 실제로는 비밀번호를 암호화하여 비교해야 합니다.
    if (!member.getPassword().equals(requestDto.getPassword())) {
      throw new IllegalArgumentException("아이디 또는 비밀번호가 올바르지 않습니다.");
    }

    // 3. 로그인 성공 후, 아이디 반환 (향후 JWT 토큰 반환 예정)
    return member.getUserId();
  }

}
