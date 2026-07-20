package ru.javabot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TelegramBotApplication {
	public static void main(String[] args) {
		System.setProperty("http.proxyHost", "157.20.121.43");
		System.setProperty("http.proxyPort", "1080");
		System.setProperty("https.proxyHost", "157.20.121.43");
		System.setProperty("https.proxyPort", "1080");

		SpringApplication.run(TelegramBotApplication.class, args);
	}
}
