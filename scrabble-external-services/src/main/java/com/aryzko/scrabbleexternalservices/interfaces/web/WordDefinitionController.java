package com.aryzko.scrabbleexternalservices.interfaces.web;

import com.aryzko.scrabbleexternalservices.application.service.WordDefinitionService;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(value = "/api/word-definition")
@RequiredArgsConstructor
public class WordDefinitionController {

    private final WordDefinitionService wordDefinitionService;

    @GetMapping("/language/{code}/word/{word}")
    public Mono<WordDefinition> getWordDefinition(@PathVariable String code, @PathVariable String word) {
        return wordDefinitionService.getWordDefinition(code, word)
                .map(definition -> WordDefinition.builder()
                        .definition(definition)
                        .build());
    }

    @Value
    @Builder
    @Jacksonized
    public static class WordDefinition {
        String definition;
    }
}
