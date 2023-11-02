package com.aryzko.scrabble.scrabbleboardmanager.domain.validator;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.CharacterSequence;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.CharacterWithPosition;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.DictionaryProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BoardValidator {

    private final BoardWordSearcher boardWordSearcher;
    private final BoardOrphanSearcher boardOrphanSearcher;
    private final DictionaryProvider dictionaryProvider;

    public BoardValidationResult validate(final Board board) {
        List<CharacterWithPosition> orphans = boardOrphanSearcher.searchOrphans(board);
        List<CharacterSequence> words = boardWordSearcher.getWordsFromBoard(board);

        Map<String, Boolean> validationStatus = dictionaryProvider.lookupEntries(words.stream()
                .map(CharacterSequence::getCharacterSequenceAsString)
                .collect(Collectors.toList()));

        return BoardValidationResult.of(orphans, words, validationStatus);
    }
}
