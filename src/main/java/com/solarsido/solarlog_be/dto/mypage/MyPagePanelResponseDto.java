package com.solarsido.solarlog_be.dto.mypage;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyPagePanelResponseDto {
  private String modelName;
  private String maker;
  private String serialNum;
  private float capability;
  private int leftLife;
}
