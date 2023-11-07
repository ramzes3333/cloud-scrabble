package com.aryzko.scrabble.scrabbleboardmanager.domain.validator;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.CharacterSequence;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.CharacterWithPosition;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Getter
@Builder
public class BoardValidationResult {

    @Builder.Default
    private final List<CharacterSequence> incorrectWords = new ArrayList<>();
    @Builder.Default
    private final List<CharacterWithPosition> orphans = new ArrayList<>();

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

    public boolean hasErrors() {
        return !CollectionUtils.isEmpty(incorrectWords) || !CollectionUtils.isEmpty(orphans);
    }
}