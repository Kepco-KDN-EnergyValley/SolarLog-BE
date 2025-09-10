package com.solarsido.solarlog_be.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "solar_panel")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SolarPanel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long panelId;

  private String modelName;
  private String maker;
  private String serialNum;
  private LocalDate installDate;
  private String installLocation;
  private int pollutionCount;
  private int faultCount;
  private double initialPower;
  private double leftLife;
  private double capability;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id")
  private User user;
}