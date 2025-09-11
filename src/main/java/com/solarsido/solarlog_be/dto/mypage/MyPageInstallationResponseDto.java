package com.solarsido.solarlog_be.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyPageInstallationResponseDto {
  private String installDate;
  private String installLocation;
  private float initialPower;
}