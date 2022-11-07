package com.aryzko.scrabble.scrabbleboardmanager.domain;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Value
@Builder
public class CharacterSequence {
    @Singular private List<CharacterWithPosition> characters;

    public String getCharacterSequenceAsString() {
        return ofNullable(characters).orElse(Collections.emptyList()).stream()
                .map(CharacterWithPosition::getCharacter)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
