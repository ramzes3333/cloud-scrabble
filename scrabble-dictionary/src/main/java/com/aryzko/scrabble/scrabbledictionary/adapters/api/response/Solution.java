package com.aryzko.scrabble.scrabbledictionary.adapters.api.response;

import lombok.Builder;
import lombok.Setter;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Setter
public class Solution {

    private List<Word> words;

    @Setter
    private static class Word {
        private List<Element> elements;

        @Setter
        private static class Element {
            private int x;
            private int y;
            private char letter;
            private boolean unmodifiableLetter;
        }
    }
}