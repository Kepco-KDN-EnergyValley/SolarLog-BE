package com.solarsido.solarlog_be.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

  private final SecretKey key;
  private final long tokenValidityInMilliseconds = 3600000; // 1시간

  public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey) {
    this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
  }

  public String createToken(String userId, String installLocation) {
    Claims claims = Jwts.claims()
        .setSubject(userId)
        .add("installLocation", installLocation)
        .build();
    Date now = new Date();
    Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(key)
        .compact();
  }

  public String getUserId(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public String getInstallLocation(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("installLocation", String.class);
  }

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