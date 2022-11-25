package com.aryzko.scrabble.scrabbleboardmanager.domain;

import lombok.Getter;

@Getter
public enum Bonus {
    DoubleWordScore(2),
    TripleWordScore(3),
    DoubleLetterScore(2),
    TripleLetterScore(3),
    None(1);

    private final int multiply;

    Bonus(int multiply) {
        this.multiply = multiply;
    }
}
