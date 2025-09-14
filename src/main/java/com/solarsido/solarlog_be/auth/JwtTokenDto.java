package com.solarsido.solarlog_be.auth;

import lombok.Getter;

@Getter
public class JwtTokenDto {
  private String accessToken;
  private final String tokenType = "Bearer";
  private String installLocation;

  public JwtTokenDto(String accessToken, String installLocation) {
    this.accessToken = accessToken;
    this.installLocation = installLocation;
  }
}