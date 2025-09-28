package com.solarsido.solarlog_be.controller;

import com.solarsido.solarlog_be.entity.PanelData;
import com.solarsido.solarlog_be.service.PanelDataService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/panel/datas")
public class PanelDataController {

  private final PanelDataService panelDataService;

  public PanelDataController(PanelDataService panelDataService) {
    this.panelDataService = panelDataService;
  }

  @PostMapping("")
  public ResponseEntity<?> saveData(@RequestBody PanelData panelData) {

    panelDataService.saveData(panelData);

    return ResponseEntity.ok(Map.of(
        "success", true,
        "message","데이터 저장 완료"
    ));

  }

}
