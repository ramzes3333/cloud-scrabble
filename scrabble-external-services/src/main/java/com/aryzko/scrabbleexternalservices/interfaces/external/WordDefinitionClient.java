package com.aryzko.scrabbleexternalservices.interfaces.external;

import reactor.core.publisher.Mono;

public interface WordDefinitionClient {

    Mono<String> getWordDefinition(String word);

    String supportedLanguageCode();
}
