package com.aryzko.scrabble.scrabbledictionary.adapters.api.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Solution {

    private List<Word> words;

    @Getter
    @Setter
    public static class Word {
        private List<Element> elements;

        @Getter
        @Setter
        public static class Element {
            private int x;
            private int y;
            private char letter;
            private boolean blank;
            private boolean unmodifiableLetter;
        }
    }
}