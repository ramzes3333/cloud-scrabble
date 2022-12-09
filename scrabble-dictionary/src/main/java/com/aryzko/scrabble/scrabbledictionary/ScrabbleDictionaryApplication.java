package com.aryzko.scrabble.scrabbledictionary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableCaching
public class ScrabbleDictionaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScrabbleDictionaryApplication.class, args);
	}

}
