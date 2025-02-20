package com.anime_social;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AnimeSocialApplication {
	public static void main(String[] args) {
		SpringApplication.run(AnimeSocialApplication.class, args);
	}
}
