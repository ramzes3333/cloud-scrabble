package com.aryzko.scrabble.scrabbleboardmanager.domain.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class Word {
    @Setter
    private int points;
    @Singular
    private List<Element> elements;
    @Setter
    @Builder.Default
    private List<Word> relatedWords = new ArrayList<>();
    @Setter
    @Builder.Default
    private List<Bonus> bonuses = new ArrayList<>();

    public String getWordAsString() {
        return ofNullable(elements).orElse(Collections.emptyList()).stream()
                .map(Element::getLetter)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    public Word transpose() {
        return Word.builder()
                .points(this.getPoints())
                .elements(this.getElements().stream()
                        .map(Element::transpose)
                        .collect(Collectors.toList()))
                .relatedWords(this.getRelatedWords().stream()
                        .map(Word::transpose)
                        .collect(Collectors.toList()))
                .bonuses(this.getBonuses())
                .build();
    }

    @Getter
    @Builder
    @EqualsAndHashCode
    public static class Element {
        private int x;
        private int y;
        private char letter;
        @Setter
        private int points;
        private boolean blank;
        private boolean onBoard;

        public Word.Element transpose() {
            return Word.Element.builder()
                    .x(this.getY())
                    .y(this.getX())
                    .letter(this.getLetter())
                    .points(this.getPoints())
                    .blank(this.isBlank())
                    .onBoard(this.isOnBoard())
                    .build();
        }
    }
}
