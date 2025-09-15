package com.solarsido.solarlog_be.entity;

import lombok.Getter;

@Getter
public enum EventType {
  FAULT("결함"),
  POLLUTION("오염");

  private final String description;

  EventType(String description) {
    this.description = description;
  }

  // 프론트에 보내는 한국어 enum 설명
  public String getDescription() {
    return description;
  }
}
