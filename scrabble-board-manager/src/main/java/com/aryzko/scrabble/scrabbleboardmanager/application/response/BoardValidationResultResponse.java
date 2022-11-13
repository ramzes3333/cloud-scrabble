package com.aryzko.scrabble.scrabbleboardmanager.application.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BoardValidationResultResponse {

    private final List<CharacterSequence> incorrectWords;
    private final List<CharacterWithPosition> orphans;

    public record CharacterSequence(List<CharacterWithPosition> characters) {

    }

    public record CharacterWithPosition(Integer x, Integer y, Character character) {
    }
}
