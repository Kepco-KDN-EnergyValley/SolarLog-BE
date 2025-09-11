package com.solarsido.solarlog_be.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  private final long tokenValidityInMilliseconds = 3600000; // 1시간

  @Value("${jwt.secret-key}")
  private String secretKeyBase64;

  private SecretKey key;

  // 실행 시점에 Base64 디코딩해서 SecretKey 생성
  @PostConstruct
  public void init() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKeyBase64);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  // JWT 토큰 생성
  public String createToken(String userId) {
    Date now = new Date();
    Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);

    return Jwts.builder()
        .subject(userId)        // "sub" claim
        .issuedAt(now)
        .expiration(validity)
        .signWith(key)
        .compact();
  }

  // JWT 토큰에서 userId 추출
  public String getUserId(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  // JWT 토큰 유효성 검증
  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}