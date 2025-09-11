package com.solarsido.solarlog_be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CorsConfig {

  @Bean
  public CorsFilter corsFilter() {
    CorsConfiguration config = new CorsConfiguration();

    // 허용할 origin (프론트 배포 URL을 넣어주세요)
    config.setAllowedOrigins(List.of(
        "http://localhost:3000"      // 프론트 로컬 개발용
              // 프론트 배포 주소
    ));

    // 허용할 HTTP 메서드
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

    // 허용할 헤더
    config.setAllowedHeaders(List.of("*"));

    // 인증정보(쿠키, Authorization 헤더) 허용 여부
    config.setAllowCredentials(true);

    // 모든 경로에 적용
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);

    return new CorsFilter(source);
  }
}
