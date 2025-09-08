package com.solarsido.solarlog_be.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "panel_roi")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PanelRoi {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long roiId;

  @ManyToOne
  @JoinColumn(name = "panel_id")
  private SolarPanel solarPanel;

  @Column(name = "shape_data_x")
  private float shapeDataX;

  @Column(name = "shape_data_y")
  private float shapeDataY;

  @Column(name = "shpae_data_w")
  private float shapeDataW;

  @Column(name = "shape_data_h")
  private float shapeDataH;

}
