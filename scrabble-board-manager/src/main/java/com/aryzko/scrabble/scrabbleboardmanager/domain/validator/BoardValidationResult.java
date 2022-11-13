package com.aryzko.scrabble.scrabbleboardmanager.domain.validator;

import com.aryzko.scrabble.scrabbleboardmanager.application.response.BoardValidationResultResponse;
import com.aryzko.scrabble.scrabbleboardmanager.domain.CharacterSequence;
import com.aryzko.scrabble.scrabbleboardmanager.domain.CharacterWithPosition;
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

    private final List<CharacterSequence> incorrectWords;
    private final List<CharacterWithPosition> orphans;

    public static BoardValidationResult of(final List<CharacterWithPosition> orphans,
                                           final List<CharacterSequence> words,
                                           final Map<String, Boolean> validationStatus) {

        return BoardValidationResult.builder()
                .incorrectWords(words.stream()
                        .filter(isWordInvalid(validationStatus))
                        .collect(Collectors.toList()))
                .orphans(orphans)
                .build();
    }

    private static Predicate<CharacterSequence> isWordInvalid(final Map<String, Boolean> validationStatus) {
        return w -> ofNullable(validationStatus.get(w.getCharacterSequenceAsString()))
                .orElseThrow(() -> new IllegalStateException("There is no word in validation result"))
                .equals(Boolean.FALSE);
    }
}