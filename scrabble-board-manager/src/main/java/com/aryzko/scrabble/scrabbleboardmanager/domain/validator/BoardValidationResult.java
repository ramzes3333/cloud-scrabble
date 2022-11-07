package com.aryzko.scrabble.scrabbleboardmanager.domain.validator;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value(staticConstructor = "of")
public class BoardValidationResult {

    private List<BoardValidationError> errors;

    @Value
    @Builder
    public static class BoardValidationError {

    }
}