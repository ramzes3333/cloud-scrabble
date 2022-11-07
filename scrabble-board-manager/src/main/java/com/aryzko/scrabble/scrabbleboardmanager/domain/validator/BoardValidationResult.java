package com.aryzko.scrabble.scrabbleboardmanager.domain.validator;

import com.aryzko.scrabble.scrabbleboardmanager.domain.CharacterSequence;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Getter
@Builder
public class BoardValidationResult {

    private List<BoardValidationError> errors;

    public static BoardValidationResult of(final List<CharacterSequence> words, final Map<String, Boolean> validationStatus) {
        return BoardValidationResult.builder()
                .errors(words.stream()
                        .filter(isWordInvalid(validationStatus))
                        .map(BoardValidationError::of)
                        .collect(Collectors.toList())).build();
    }

    private static Predicate<CharacterSequence> isWordInvalid(final Map<String, Boolean> validationStatus) {
        return w -> ofNullable(validationStatus.get(w.getCharacterSequenceAsString()))
                .orElseThrow(() -> new IllegalStateException("There is no word in validation result"))
                .equals(Boolean.FALSE);
    }

    @Value(staticConstructor = "of")
    public static class BoardValidationError {
        private final CharacterSequence incorrectWord;
    }
}