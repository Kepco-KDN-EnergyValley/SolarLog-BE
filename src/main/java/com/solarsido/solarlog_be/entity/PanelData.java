package com.solarsido.solarlog_be.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.ZonedDateTime;
import lombok.Setter;

@Entity
@Table(name = "panel_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PanelData {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long dataId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "panel_id") // 외래 키 설정
  private SolarPanel solarPanel;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Asia/Seoul")
  @Column(nullable = false)
  private ZonedDateTime measuredDate;

  private float voltage;
  private float current;
  private float power;
}