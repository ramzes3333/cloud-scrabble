package com.aryzko.scrabble.scrabbledictionary.adapters.api.request;

import lombok.Getter;

import java.util.List;

@Getter
public class Line {
    private List<LineField> fields;

    @Getter
    public static class LineField {
        private int x;
        private int y;
        private Character letter;
        private boolean anchor;
        private boolean anyLetter;
        private Integer leftLimit;
        private List<Character> availableLetters;

        boolean isLetter() {
            return letter != null;
        }
    }
}
