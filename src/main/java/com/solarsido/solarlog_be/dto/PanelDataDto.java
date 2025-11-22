package com.solarsido.solarlog_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
  @JsonFormat(shape = JsonFormat.Shape.STRING,
      pattern = "yyyy-MM-dd'T'HH:mm:ss",
      timezone = "Asia/Seoul")
  private LocalDateTime measuredDate;
  private Float voltage;
  private Float current;
  private Float power;

}
