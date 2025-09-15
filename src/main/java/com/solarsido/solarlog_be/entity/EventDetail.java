package com.solarsido.solarlog_be.entity;

import lombok.Getter;

@Getter
public enum EventDetail {
  BIRD_DROPPINGS("조류 배설물"),
  DUST("먼지"),
  SNOW("눈 쌓임"),
  ELECTRICAL("전기적 결함"),
  PHYSICAL("물리적 결함");

  private final String description;

  EventDetail(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
