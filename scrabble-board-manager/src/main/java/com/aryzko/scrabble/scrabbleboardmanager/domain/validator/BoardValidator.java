package com.aryzko.scrabble.scrabbleboardmanager.domain.validator;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.CharacterSequence;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.DictionaryProvider;
import com.aryzko.scrabble.scrabbleboardmanager.domain.service.BoardInspector;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BoardValidator {

    private final BoardInspector boardInspector;
    private final DictionaryProvider dictionaryProvider;

    public BoardValidationResult validate(Board board) {
        List<CharacterSequence> words = boardInspector.getWordsFromBoard(board);

        Map<String, Boolean> validationStatus = dictionaryProvider.lookupEntries(words.stream()
                .map(CharacterSequence::getCharacterSequenceAsString)
                .collect(Collectors.toList()));

        return BoardValidationResult.of(words, validationStatus);
    }
}
