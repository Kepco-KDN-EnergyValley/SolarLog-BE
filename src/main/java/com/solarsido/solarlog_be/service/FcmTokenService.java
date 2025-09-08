package com.solarsido.solarlog_be.service;

import com.solarsido.solarlog_be.entity.Fcm;
import com.solarsido.solarlog_be.entity.User;
import com.solarsido.solarlog_be.repository.FcmTokenRepository;
import com.solarsido.solarlog_be.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class FcmTokenService {

  private final FcmTokenRepository fcmTokenRepository;
  private final UserRepository userRepository;

  public FcmTokenService(FcmTokenRepository fcmTokenRepository, UserRepository userRepository) {
    this.fcmTokenRepository = fcmTokenRepository;
    this.userRepository = userRepository;
  }

  //fcm 토큰 등록
  public void registerToken(String userId, String fcmToken) {
    // 유저id에 맞는 user 객체 찾기
    User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("User not found"));

    // 중복확인하고 fcm 토큰 저장
    if(!fcmTokenRepository.existsByUserAndFcmToken(user,fcmToken)){
      Fcm fcm = new Fcm();
      fcm.setUser(user); // user에 맞는 id 저장
      fcm.setFcmToken(fcmToken);
      fcmTokenRepository.save(fcm); // id랑 토큰을 FCM 레포(db)에 저장
    }

  }

}
