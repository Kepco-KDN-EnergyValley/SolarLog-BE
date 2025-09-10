package com.solarsido.solarlog_be.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PanelRoiRequestDto {

  private Long panelId;
  private List<CoordinateDto> coordinates;

  @Getter
  @Setter
  public static class CoordinateDto{
    private float x;
    private float y;
    private float w;
    private float h;
  }

}
