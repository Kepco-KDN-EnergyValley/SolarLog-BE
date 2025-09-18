package com.solarsido.solarlog_be.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AiDetectionDto {
  private String eventType;
  private String eventDetail;
  private Float bboxX;
  private Float bboxY;
  private Float bboxW;
  private Float bboxH;
  private String image;

}
