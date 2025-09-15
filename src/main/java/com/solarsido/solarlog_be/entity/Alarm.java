package com.solarsido.solarlog_be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "alarm")
public class Alarm {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "alarm_id")
  private Long alarmId;

  @Column(name = "alarm_date")
  private LocalDateTime alarmDate;

  @ManyToOne
  @JoinColumn(name = "panel_id")
  private SolarPanel solarPanel;

  @ManyToOne
  @JoinColumn(name = "event_id")
  private AiDetectionEvent aiDetectionEvent;

  @Column(name = "is_read")
  private boolean isRead;

  public void ChangeIsRead() {
    this.isRead = true;
  }


}
