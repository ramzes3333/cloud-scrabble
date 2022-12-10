package com.aryzko.scrabblegame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ScrabbleGameApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScrabbleGameApplication.class, args);
    }

}
