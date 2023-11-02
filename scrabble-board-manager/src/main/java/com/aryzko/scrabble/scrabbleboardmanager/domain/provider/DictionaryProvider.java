package com.aryzko.scrabble.scrabbleboardmanager.domain.provider;

import lombok.Value;

import java.util.List;
import java.util.Map;

public interface DictionaryProvider {

    Map<String, Boolean> lookupEntries(List<String> values);

    List<Character> fillGap(String pattern);

    Solution resolve(PreparedLine preparedLine, List<Character> availableLetters);

    @Value
    class PreparedLine {
        List<LineField> fields;

        @Value
        public static class LineField {
            int x;
            int y;
            Character letter;
            boolean anchor;
            boolean anyLetter;
            Integer leftLimit;
            List<Character> availableLetters;
        }
    }

    @Value
    class Solution {
        List<Word> words;

        @Value
        public static class Word {
            List<Element> elements;

            @Value
            public static class Element {
                int x;
                int y;
                char letter;
                boolean blank;
                boolean unmodifiableLetter;
            }
        }
    }
}
