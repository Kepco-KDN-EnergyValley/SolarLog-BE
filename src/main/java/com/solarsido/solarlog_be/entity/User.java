package com.solarsido.solarlog_be.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String userId;
  private String password;

  // 유저가 여러 개의 FCM 토큰을 가질 수 있음
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Fcm> fcmTokens = new ArrayList<>();

  public User() {
  }

  public User(String userId, String password) {
    this.userId = userId;
    this.password = password;
  }

  // --- Getter와 Setter ---

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
  public List<Fcm> getFcmTokens() {   // ✅ 이게 필요함
    return fcmTokens;
  }

  public void setFcmTokens(List<Fcm> fcmTokens) {
    this.fcmTokens = fcmTokens;
  }
}
