package com.aryzko.scrabble.scrabbleboardmanager.infrastructure.configuration.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("boards", "tileConfiguration");
        cacheManager.setCaffeine(Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.SECONDS));
        return cacheManager;
    }
}