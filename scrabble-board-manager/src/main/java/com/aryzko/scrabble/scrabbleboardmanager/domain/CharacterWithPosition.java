package com.aryzko.scrabble.scrabbleboardmanager.domain;

import lombok.Value;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Value
@SuperBuilder
public class CharacterWithPosition extends Position {
    private Optional<Character> character;
}
