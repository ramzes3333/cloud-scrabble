package com.aryzko.scrabble.scrabbleboardmanager.domain.validator;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.CharacterSequence;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.CharacterWithPosition;
import com.aryzko.scrabble.scrabbleboardmanager.domain.common.BoardInspector;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class BoardWordSearcher {
    public static final char EMPTY_CHAR = ' ';

    public List<CharacterSequence> getWordsFromBoard(final Board board) {
        return BoardInspector.getAllBoardLines(board).stream()
                .flatMap(this::getWords)
                .collect(Collectors.toList());
    }

    private Stream<CharacterSequence> getWords(CharacterSequence line) {
        Stream.Builder<CharacterSequence> streamBuilder = Stream.builder();

        CharacterSequence.CharacterSequenceBuilder characterSequenceBuilder = CharacterSequence.builder();
        for(CharacterWithPosition ch : line.getCharacters()) {
            if(ch.getCharacter().isPresent()) {
                characterSequenceBuilder.character(ch);
            } else {
                addWordToStreamIfLengthIsGreaterThanOne(streamBuilder, characterSequenceBuilder);
                characterSequenceBuilder = CharacterSequence.builder();
            }
        }
        addWordToStreamIfLengthIsGreaterThanOne(streamBuilder, characterSequenceBuilder);
        return streamBuilder.build();
    }

    private static void addWordToStreamIfLengthIsGreaterThanOne(Stream.Builder<CharacterSequence> builder, CharacterSequence.CharacterSequenceBuilder characterSequenceBuilder) {
        CharacterSequence characterSequence = characterSequenceBuilder.build();
        if(characterSequence.getCharacters().size() > 1) {
            builder.add(characterSequence);
        }
    }
}
