package com.aryzko.scrabblegame.application.model.board;

import lombok.Value;

import java.util.List;

@Value
public class BoardValidationResultResponse {

    private final List<CharacterSequence> incorrectWords;
    private final List<CharacterWithPosition> orphans;

    public record CharacterSequence(List<CharacterWithPosition> characters) {
    }

    public record CharacterWithPosition(Integer x, Integer y, Character character) {
    }
}
