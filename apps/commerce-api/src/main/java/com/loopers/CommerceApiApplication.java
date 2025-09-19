package com.loopers;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import jakarta.annotation.PostConstruct;

@ConfigurationPropertiesScan
@EnableFeignClients
@EnableScheduling
@SpringBootApplication
public class CommerceApiApplication {

	@PostConstruct
	public void started() {
		// set timezone
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

	public static void main(String[] args) {
		SpringApplication.run(CommerceApiApplication.class, args);
	}
}
