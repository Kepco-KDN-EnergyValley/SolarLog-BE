package com.solarsido.solarlog_be.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PanelDataDto {

  private Long panelId;
  private LocalDateTime measuredDate;
  private Float voltage;
  private Float current;
  private Float power;

}
