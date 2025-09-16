package com.solarsido.solarlog_be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "panel_data")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PanelData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long dataId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "panel_id") // 외래 키 설정
  private SolarPanel solarPanel;

  private LocalDateTime measuredDate;
  private float voltage;
  private float current;
  private float power;
  private float co2;
}