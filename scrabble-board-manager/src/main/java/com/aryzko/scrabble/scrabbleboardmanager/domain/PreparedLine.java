package com.aryzko.scrabble.scrabbleboardmanager.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class PreparedLine {
    @Singular
    private List<LineField> fields;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PreparedLine(@JsonProperty("fields") List<LineField> fields) {
        this.fields = fields;
    }

    @Value
    @Builder
    public static class LineField {
        private int x;
        private int y;
        private Character letter;
        private boolean anchor;
        private boolean anyLetter;
        private Integer leftLimit;
        private List<Character> availableLetters;

        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        public LineField(@JsonProperty("x") int x,
                         @JsonProperty("y") int y,
                         @JsonProperty("letter") Character letter,
                         @JsonProperty("anchor") boolean anchor,
                         @JsonProperty("anyLetter") boolean anyLetter,
                         @JsonProperty("leftLimit") Integer leftLimit,
                         @JsonProperty("availableLetters") List<Character> availableLetters) {
            this.x = x;
            this.y = y;
            this.letter = letter;
            this.anchor = anchor;
            this.anyLetter = anyLetter;
            this.leftLimit = leftLimit;
            this.availableLetters = availableLetters;
        }

        boolean isLetter() {
            return letter != null;
        }
    }
}
