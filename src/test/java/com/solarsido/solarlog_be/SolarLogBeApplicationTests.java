package com.solarsido.solarlog_be;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SolarLogBeApplicationTests {

  @Test
  void contextLoads() {
  }

}
