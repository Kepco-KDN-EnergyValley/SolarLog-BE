package com.solarsido.solarlog_be.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter // 모든 필드의 Getter 메소드를 자동 생성
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자 자동 생성
public class UserJoinRequestDto {

  // 회원 정보
  private String userId;
  private String password;

  // 태양광 패널 정보
  private String modelName;
  private String maker;
  private String serialNum;
  private String installDate;
  private String installLocation;
  private float initialPower;

}
