package com.example.java_tutorial.config;

import org.springframework.core.io.ClassPathResource;
import java.io.InputStream;
import java.io.IOException;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccount = new ClassPathResource("tracky-21023-firebase-adminsdk-fbsvc-70c6359056.json")
                    .getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
