package com.solarsido.solarlog_be.repository;

import com.solarsido.solarlog_be.entity.Fcm;
import com.solarsido.solarlog_be.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FcmTokenRepository extends JpaRepository<Fcm,Long> {
  // (id, token) 중복 확인
  boolean existsByUserAndFcmToken(User user, String fcmToken);
  // 해당 토큰으로 알림 전송 실패 시 해당 토큰 삭제
  void deleteByFcmToken(String fcmToken);

}
