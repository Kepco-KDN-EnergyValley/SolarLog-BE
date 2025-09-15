package com.solarsido.solarlog_be.dto;

import lombok.Getter;

@Getter
public class ApiResponseDto<T> {
  private final boolean success;
  private final T data;
  private final String message;

  // 성공 응답 (데이터가 있는 경우)
  public ApiResponseDto(T data) {
    this.success = true;
    this.data = data;
    this.message = null;
  }

  // 성공 응답 (데이터 없이 메시지만 있는 경우)
  public ApiResponseDto(String message) {
    this.success = true;
    this.data = null;
    this.message = message;
  }

  // 실패 응답
  public ApiResponseDto(boolean success, String message) {
    this.success = success;
    this.data = null;
    this.message = message;
  }
}