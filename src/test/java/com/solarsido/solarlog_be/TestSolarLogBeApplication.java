package com.solarsido.solarlog_be;

import org.springframework.boot.SpringApplication;

public class TestSolarLogBeApplication {

  public static void main(String[] args) {
    SpringApplication.from(SolarLogBeApplication::main).with(TestcontainersConfiguration.class)
        .run(args);
  }

}
