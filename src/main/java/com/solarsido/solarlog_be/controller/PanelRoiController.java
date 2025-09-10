package com.solarsido.solarlog_be.controller;

import com.solarsido.solarlog_be.dto.PanelRoiRequestDto;
import com.solarsido.solarlog_be.dto.SnapShotRequestDto;
import com.solarsido.solarlog_be.service.PanelRoiService;
import com.solarsido.solarlog_be.service.SnapShotService;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/panelRoi")
public class PanelRoiController {

  private final PanelRoiService panelRoiService;
  private final SnapShotService snapShotService;

  public PanelRoiController(PanelRoiService panelRoiService, SnapShotService snapShotService) {
    this.panelRoiService = panelRoiService;
    this.snapShotService = snapShotService;
  }
  // 태양광 패널 초기 사진 받기
  @PostMapping("/snapshot")
  public ResponseEntity<String> saveSnapshot(@RequestBody SnapShotRequestDto snapShotRequestDto) {
   String result = snapShotService.saveSnapshotAndRunPython(snapShotRequestDto.getUrl());
   return ResponseEntity.ok(result);
  }

  // 좌표 초기 등록
  @PostMapping("/coordinates")
  public ResponseEntity<?> saveRoi(@RequestBody PanelRoiRequestDto panelRoiRequestDto) {
    panelRoiService.saveRoi(panelRoiRequestDto);
    return ResponseEntity.ok(Map.of(
        "success", true,
        "message", "Roi 좌표 등록 완료"
    ));
  }
}
