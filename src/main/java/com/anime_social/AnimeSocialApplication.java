package com.anime_social;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class AnimeSocialApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnimeSocialApplication.class, args);
	}

}
