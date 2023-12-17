package com.aryzko.scrabbleexternalservices.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientsConfiguration {

    @Bean
    public WebClient sjpWebClient() {
        return WebClient.builder()
                .baseUrl("https://sjp.pl/")
                .build();
    }
}
