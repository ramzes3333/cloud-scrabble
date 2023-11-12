package com.aryzko.scrabble.scrabbleboardmanager.domain.model;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class PreparedLine {
    @Singular
    List<LineField> fields;

    public PreparedLine(List<LineField> fields) {
        this.fields = fields;
    }

    @Value
    @Builder
    @Jacksonized
    public static class LineField {
        int x;
        int y;
        Character letter;
        boolean anchor;
        boolean anyLetter;
        Integer leftLimit;
        List<Character> availableLetters;

        boolean isLetter() {
            return letter != null;
        }
    }
}
