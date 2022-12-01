package com.aryzko.scrabble.scrabbleboardmanager.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import org.apache.commons.lang.NotImplementedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Getter
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
        @Setter
        @Builder.Default
        private List<Word> relatedWords = new ArrayList<>();

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

    public Solution transpose(TransposeType transposeType) {
        Solution transposed = Solution.builder()
                .words(words.stream()
                        .map(word -> transpose(word, transposeType))
                        .collect(Collectors.toList()))
                .build();
        return transposed;
    }

    private Solution.Word transpose(Solution.Word word, TransposeType transposeType) {
        Solution.Word transposed = Word.builder()
                .points(word.getPoints())
                .elements(word.getElements().stream()
                        .map(e -> transpose(e, transposeType))
                        .collect(Collectors.toList()))
                .relatedWords(word.getRelatedWords().stream()
                        .map(w -> transpose(w, transposeType))
                        .collect(Collectors.toList()))
                .build();
        return transposed;
    }

    private Solution.Word.Element transpose(Solution.Word.Element element, TransposeType transposeType) {
        if(transposeType == TransposeType.FLIP_HORIZONTALLY_AND_ROTATE_LEFT) {
            throw new NotImplementedException();
        }

        Solution.Word.Element transposed = Word.Element.builder()
                .x(element.getY())
                .y(element.getX())
                .letter(element.getLetter())
                .points(element.getPoints())
                .onBoard(element.isOnBoard())
                .build();

        return transposed;
    }

    public enum TransposeType {
        FLIP_HORIZONTALLY_AND_ROTATE_LEFT,
        FLIP_HORIZONTALLY_AND_ROTATE_RIGHT
    }
}
