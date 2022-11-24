package com.aryzko.scrabble.scrabbledictionary.domain.model.resolver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class Line {
    private List<LineField> fields;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Line(@JsonProperty("fields") List<LineField> fields) {
        this.fields = fields;

        if(fields != null && fields.size() > 1) {
            for (int i = 0; i < fields.size(); i++) {
                if(i != 0) {
                    fields.get(i).setPrev(fields.get(i - 1));
                }
                if(i != fields.size()-1) {
                    fields.get(i).setNext(fields.get(i + 1));
                }
            }
        }
    }

    @Getter
    public static class LineField {
        private int x;
        private int y;
        private Character letter;
        private boolean anchor;
        private boolean anyLetter;
        private Integer leftLimit;
        private List<Character> availableLetters;

        @JsonIgnore
        @Getter
        @Setter
        private LineField prev;

        @JsonIgnore
        @Getter
        @Setter
        private LineField next;

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

        public boolean isLetter() {
            return letter != null;
        }
    }
}
