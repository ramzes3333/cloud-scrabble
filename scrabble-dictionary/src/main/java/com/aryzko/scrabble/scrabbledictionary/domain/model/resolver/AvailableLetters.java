package com.aryzko.scrabble.scrabbledictionary.domain.model.resolver;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Getter
@Builder
public class AvailableLetters {
    @Singular
    private List<Character> characters;
}
