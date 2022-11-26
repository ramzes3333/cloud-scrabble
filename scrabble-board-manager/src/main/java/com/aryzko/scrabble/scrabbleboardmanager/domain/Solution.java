package com.aryzko.scrabble.scrabbleboardmanager.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.Value;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Value
@Builder
public class Solution {
    private List<Word> words;

    @Getter
    @Builder
    public static class Word {
        @Setter
        private int points;
        @Singular
        private List<Element> elements;

        public String getWordAsString() {
            return ofNullable(elements).orElse(Collections.emptyList()).stream()
                    .map(Element::getLetter)
                    .map(String::valueOf)
                    .collect(Collectors.joining());
        }

        @Getter
        @Builder
        public static class Element {
            private int x;
            private int y;
            private char letter;
            @Setter
            private int points;
            private boolean onBoard;
        }
    }
}
