package com.solarsido.solarlog_be.auth;

import lombok.Getter;

@Getter
public class JwtTokenDto {
  private String accessToken;
  private final String tokenType = "Bearer";

  public JwtTokenDto(String accessToken) {
    this.accessToken = accessToken;
  }
}