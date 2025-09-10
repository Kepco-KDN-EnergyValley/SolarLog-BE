package com.solarsido.solarlog_be.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfig {
  @Bean
  public FirebaseApp firebaseApp() throws IOException {
    // resources 폴더에서 JSON 파일 읽기
    InputStream serviceAccount =
        getClass().getClassLoader().getResourceAsStream("firebase-service-key.json");

    FirebaseOptions options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

    return FirebaseApp.initializeApp(options);
  }


}
