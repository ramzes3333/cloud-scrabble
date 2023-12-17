package com.aryzko.scrabbleexternalservices.application.service;

import com.aryzko.scrabbleexternalservices.application.provider.WordDefinitionProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WordDefinitionService {

    private final WordDefinitionProvider wordDefinitionProvider;

    public Mono<String> getWordDefinition(String languageCode, String word) {
        return wordDefinitionProvider.getWordDefinition(languageCode, word);
    }
}
