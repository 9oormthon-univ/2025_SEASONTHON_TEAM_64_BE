package org.goormthon.seasonthon.nocheongmaru.global.config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import jakarta.annotation.PostConstruct;

@Configuration
public class FirebaseConfig {
	@Value("${firebase.config-path}")
	private String firebaseConfigPath;

	@PostConstruct
	public void init() throws IOException {
		try (FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath)) {
			FirebaseOptions options = FirebaseOptions.builder()
				.setCredentials(GoogleCredentials.fromStream(serviceAccount))
				.build();
			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
			}
		}
	}

	@Bean
	public FirebaseMessaging firebaseMessaging() {
		return FirebaseMessaging.getInstance(FirebaseApp.getInstance());
	}
}
