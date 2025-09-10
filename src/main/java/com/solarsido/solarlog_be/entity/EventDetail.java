package com.solarsido.solarlog_be.entity;

import lombok.Getter;

@Getter
public enum EventDetail {
  Bird_droppings("조류 배설물"),
  Dust("먼지"),
  Snow("눈 쌓임"),
  Electrical("전기적 결함"),
  Physical("물리적 결함");

  private final String description;

  EventDetail(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
