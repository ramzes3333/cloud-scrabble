package com.aryzko.scrabble.scrabbleboardmanager.application.response;

import lombok.Data;

import java.util.List;

@Data
public class Solution {
    private List<Word> words;

    public record Word (int points, List<Element> elements, List<Word> relatedWords, List<Bonus> bonuses) { }

    public record Element (int x, int y, char letter, int points, boolean blank, boolean onBoard) { }

    public enum Bonus {
        DoubleWordScore,
        TripleWordScore,
        DoubleLetterScore,
        TripleLetterScore,
        None
    }
}
