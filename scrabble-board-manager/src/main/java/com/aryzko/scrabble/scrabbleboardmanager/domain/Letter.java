package com.aryzko.scrabble.scrabbleboardmanager.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Letter {
    private Character letter;
    private Integer points;
    private boolean blank;

    protected Letter clone() {
        Letter clone = new Letter();
        clone.setLetter(letter);
        clone.setPoints(points);
        clone.setBlank(blank);
        return clone;
    }
}
