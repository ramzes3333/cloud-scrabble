package com.aryzko.scrabble.scrabbletilemanager;

import io.mongock.runner.springboot.EnableMongock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMongock
public class ScrabbleTileManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScrabbleTileManagerApplication.class, args);
	}

}
