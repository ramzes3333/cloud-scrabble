package com.aryzko.scrabble.scrabbleboardmanager.domain;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Value
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class CharacterWithPosition extends Position {
    private Optional<Character> character;

    public boolean isCharSet() {
        return character.isPresent();
    }
}
