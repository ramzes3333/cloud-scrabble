package com.aryzko.scrabble.scrabbleboardmanager.domain;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class Solution {
    @Singular
    private List<Word> words;

    @Value
    @Builder
    public static class Word {
        private int points;
        private List<Element> elements;

        @Value
        @Builder
        public static class Element {
            private int x;
            private int y;
            private char letter;
            private boolean onBoard;
        }
    }
}
