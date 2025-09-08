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

@Entity
@Table(name = "ai_detection_event")
public class AiDetectionEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long eventId;

  @Enumerated(EnumType.STRING)
  private EventType eventType;

  @Enumerated(EnumType.STRING)
  private EventDetail eventDetail;

  @Column(name = "bboxX")
  private float bboxX;

  @Column(name = "bboxY")
  private float bboxY;

  @Column(name = "bboxW")
  private float bboxW;

  @Column(name = "bboxH")
  private float bboxH;

  @Column(name = "image")
  private String image;

  @ManyToOne
  @JoinColumn(name = "matched_panel_id")
  private SolarPanel solarPanel;

  @ManyToOne
  @JoinColumn(name = "matched_roi_id")
  private PanelRoi panelRoi;



}
