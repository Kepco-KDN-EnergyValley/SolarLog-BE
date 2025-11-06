package com.solarsido.solarlog_be.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX", timezone = "Asia/Seoul")
  @Column(nullable = false)
  private ZonedDateTime measuredDate;
  private Float voltage;
  private Float current;
  private Float power;

}
