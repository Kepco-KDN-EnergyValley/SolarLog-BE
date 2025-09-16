package com.solarsido.solarlog_be.controller;

import com.solarsido.solarlog_be.dto.ApiResponseDto;
import com.solarsido.solarlog_be.auth.JwtTokenDto;
import com.solarsido.solarlog_be.dto.member.CheckIdRequestDto;
import com.solarsido.solarlog_be.dto.member.LoginRequestDto;
import com.solarsido.solarlog_be.dto.member.UserJoinRequestDto;
import com.solarsido.solarlog_be.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/api/v1/signup")
  public ResponseEntity<ApiResponseDto<Void>> join(@Valid @RequestBody UserJoinRequestDto requestDto) {
    userService.join(requestDto);
    return new ResponseEntity<>(new ApiResponseDto<>("회원가입이 완료되었습니다."), HttpStatus.CREATED);
  }

  @GetMapping("/api/v1/signup/check-id")
  public ResponseEntity<ApiResponseDto<Void>> checkDuplication(@RequestParam String userId) {
    boolean isDuplicated = userService.checkUserIdDuplication(new CheckIdRequestDto(userId));

    if (isDuplicated) {
      // success를 false로 설정하는 생성자 호출
      return new ResponseEntity<>(new ApiResponseDto<>(false, "이미 존재하는 아이디입니다."), HttpStatus.CONFLICT);
    } else {
      // success를 true로 설정하는 생성자 호출
      return new ResponseEntity<>(new ApiResponseDto<>("사용 가능한 아이디입니다."), HttpStatus.OK);
    }
  }

  @PostMapping("/api/v1/users/login")
  public ResponseEntity<ApiResponseDto<JwtTokenDto>> login(@Valid @RequestBody LoginRequestDto requestDto) {
    try {
      JwtTokenDto jwtTokenDto = userService.login(requestDto);
      return new ResponseEntity<>(new ApiResponseDto<>(jwtTokenDto), HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      // success를 false로, 메시지를 반환하도록 수정
      return new ResponseEntity<>(new ApiResponseDto<>(false, "아이디 또는 비밀번호가 올바르지 않습니다."), HttpStatus.UNAUTHORIZED);
    }
  }
}