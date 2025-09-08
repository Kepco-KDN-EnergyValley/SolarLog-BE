package com.solarsido.solarlog_be.controller;

import com.solarsido.solarlog_be.dto.member.CheckIdRequestDto;
import com.solarsido.solarlog_be.dto.member.UserJoinRequestDto;
import com.solarsido.solarlog_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.solarsido.solarlog_be.dto.member.LoginRequestDto;

//JWT 추가
import com.solarsido.solarlog_be.auth.JwtTokenDto;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService memberService;

  // 1. 회원가입 API
  @PostMapping("/api/v1/signup")
  public ResponseEntity<String> join(@RequestBody UserJoinRequestDto requestDto) {
    memberService.join(requestDto);
    return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.CREATED);
  }

  // 2. 아이디 중복 확인 API
  @GetMapping("/api/v1/signup/check-id")
  public ResponseEntity<String> checkDuplication(@RequestParam String userId) {
    boolean isDuplicated = memberService.checkUserIdDuplication(new CheckIdRequestDto(userId));

    if (isDuplicated) {
      return new ResponseEntity<>("이미 존재하는 아이디입니다.", HttpStatus.CONFLICT);
    } else {
      return new ResponseEntity<>("사용 가능한 아이디입니다.", HttpStatus.OK);
    }
  }

  // 3. 로그인 API 추가
  @PostMapping("/api/v1/users/login")
  public ResponseEntity<JwtTokenDto> login(@RequestBody LoginRequestDto requestDto) {
    try {
      JwtTokenDto jwtTokenDto = memberService.login(requestDto); // DTO를 직접 받아옴
      return new ResponseEntity<>(jwtTokenDto, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }
  }
}
