package com.anime_social;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan(basePackages = "com.anime_social.entity")
public class AnimeSocialApplication {
	public static void main(String[] args) {
		SpringApplication.run(AnimeSocialApplication.class, args);
	}
}
