package com.aryzko.scrabble.scrabblegateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@SpringBootApplication
@EnableWebFluxSecurity
public class ScrabbleGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScrabbleGatewayApplication.class, args);
    }

}
