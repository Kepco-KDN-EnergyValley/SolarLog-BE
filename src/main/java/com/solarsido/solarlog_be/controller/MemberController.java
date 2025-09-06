package com.solarsido.solarlog_be.controller;

import com.solarsido.solarlog_be.dto.member.CheckIdRequestDto;
import com.solarsido.solarlog_be.dto.member.MemberJoinRequestDto;
import com.solarsido.solarlog_be.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.solarsido.solarlog_be.dto.member.LoginRequestDto;

@RestController
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  // 1. 회원가입 API (URL 수정)
  @PostMapping("/api/v1/signup")
  public ResponseEntity<String> join(@RequestBody MemberJoinRequestDto requestDto) {
    memberService.join(requestDto);
    return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.CREATED);
  }

  // 2. 아이디 중복 확인 API (URL 수정)
  @GetMapping("/api/v1/signup/check-id")
  public ResponseEntity<String> checkDuplication(@RequestParam String userId) {
    boolean isDuplicated = memberService.checkUserIdDuplication(new CheckIdRequestDto(userId));

    if (isDuplicated) {
      return new ResponseEntity<>("이미 존재하는 아이디입니다.", HttpStatus.CONFLICT);
    } else {
      return new ResponseEntity<>("사용 가능한 아이디입니다.", HttpStatus.OK);
    }
  }

  // 3. 로그인 API 추가 (URL 수정)
  @PostMapping("/api/v1/users/login")
  public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto) {
    try {
      String loggedInUserId = memberService.login(requestDto);
      return new ResponseEntity<>("로그인에 성공했습니다. 사용자: " + loggedInUserId, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>("로그인 실패: " + e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
  }
}
