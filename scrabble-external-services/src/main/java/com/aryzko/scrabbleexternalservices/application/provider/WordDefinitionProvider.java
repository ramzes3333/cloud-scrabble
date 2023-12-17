package com.aryzko.scrabbleexternalservices.application.provider;

import reactor.core.publisher.Mono;

public interface WordDefinitionProvider {

    Mono<String> getWordDefinition(String languageCode, String word);
}
