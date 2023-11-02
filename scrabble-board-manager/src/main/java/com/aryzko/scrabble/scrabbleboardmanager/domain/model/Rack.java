package com.aryzko.scrabble.scrabbleboardmanager.domain.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Rack {
    private String playerId;
    private List<Letter> letters;

    protected Rack clone() {
        Rack clone = new Rack();
        clone.setLetters(letters.stream()
                .map(Letter::clone)
                .collect(Collectors.toList()));
        return clone;
    }
}

