package com.aryzko.scrabble.scrabbledictionary.domain.model.resolver;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Value
@Builder
public class Solution {
    @Singular
    private List<Word> words;

    @Value
    @Builder
    public static class Word {
        @Singular
        private List<Element> elements;

        public String getWordAsString() {
            return ofNullable(elements).orElse(Collections.emptyList()).stream()
                    .map(Element::getLetter)
                    .map(String::valueOf)
                    .collect(Collectors.joining());
        }

        @Value
        @Builder
        public static class Element {
            private int x;
            private int y;
            private char letter;
            private boolean unmodifiableLetter;
        }
    }
}