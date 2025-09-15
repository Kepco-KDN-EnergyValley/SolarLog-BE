package com.solarsido.solarlog_be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "ai_detection_event")
public class AiDetectionEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long eventId;

  @Enumerated(EnumType.STRING)
  @Column(name = "event_type", nullable = false)
  private EventType eventType;

  @Enumerated(EnumType.STRING)
  @Column(name = "event_detail", nullable = false)
  private EventDetail eventDetail;

  @Column(name = "bboxx")
  private Float bboxX;

  @Column(name = "bboxy")
  private Float bboxY;

  @Column(name = "bboxw")
  private Float bboxW;

  @Column(name = "bboxh")
  private Float bboxH;

  @Column(name = "image")
  private String image;

  @ManyToOne
  @JoinColumn(name = "matched_panel_id")
  private SolarPanel solarPanel;

  @ManyToOne
  @JoinColumn(name = "matched_roi_id")
  private PanelRoi panelRoi;



}
