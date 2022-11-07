package com.aryzko.scrabble.scrabbleboardmanager.domain;

import lombok.Value;
import lombok.experimental.SuperBuilder;

@Value
@SuperBuilder
public class CharacterWithPosition extends Position {
    private Character character;
}
