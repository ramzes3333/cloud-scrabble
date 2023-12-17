package com.aryzko.scrabbleexternalservices.interfaces.external;

import com.aryzko.scrabbleexternalservices.application.provider.WordDefinitionProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultWordDefinitionProvider implements WordDefinitionProvider {

    private final WordDefinitionClientFactory wordDefinitionClientFactory;

    @Override
    public Mono<String> getWordDefinition(String languageCode, String word) {
        return wordDefinitionClientFactory.getClient(languageCode).getWordDefinition(word);
    }

    @Component
    @RequiredArgsConstructor
    public static class WordDefinitionClientFactory {
        private final List<WordDefinitionClient> wordDefinitionClients;

        public WordDefinitionClient getClient(String languageCode) {
            return wordDefinitionClients.stream()
                    .filter(c -> c.supportedLanguageCode().equalsIgnoreCase(languageCode))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Word definition client not found for language code: %s".formatted(languageCode)));
        }
    }
}
