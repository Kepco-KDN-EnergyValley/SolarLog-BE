package com.solarsido.solarlog_be.scheduler;

import com.solarsido.solarlog_be.entity.SolarPanel;
import com.solarsido.solarlog_be.service.LifeTimeSerivce;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LifeTimeScheduler {

  private final RestTemplate restTemplate;
  private final LifeTimeSerivce lifeTimeSerivce;

  public LifeTimeScheduler(LifeTimeSerivce lifeTimeSerivce) {
    this.restTemplate = new RestTemplate();
    this.lifeTimeSerivce = lifeTimeSerivce;
  }
  @Scheduled(cron = "0 0 1 1 * ?")
  public void calculateLifeTime(){
    //1.패널 리스트 조회
    List<SolarPanel> panels = lifeTimeSerivce.findAllPanels();

    // 2. 지난 달 기준으로 history 생성
    YearMonth lastMonth = YearMonth.now().minusMonths(1);

    for (SolarPanel panel : panels) {
      Map<String, Object> body = new HashMap<>();
      body.put("panelId", panel.getPanelId());
      body.put("initialPower", panel.getInitialPower());
      body.put("history", lifeTimeSerivce.getMonthlyHistory(panel.getPanelId(), lastMonth));

      // 3. AI 서버 API 호출
      ResponseEntity<Map> response =
          restTemplate.postForEntity(
              "http://localhost:8080/api/v1/lifetime/calculate",
              body,
              Map.class
          );

      // 4. 결과 DB 업데이트
      if (response.getBody() != null && response.getBody().get("leftLife") != null) {
        Integer leftLife = (Integer) response.getBody().get("leftLife");
        lifeTimeSerivce.updateLifeTime(panel.getPanelId(), leftLife);
      }
    }
  }

}
