package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.CharacterSequence;
import com.aryzko.scrabble.scrabbleboardmanager.domain.CharacterWithPosition;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Position;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

@Component
public class BoardInspector {
    public static final char EMPTY_CHAR = ' ';

    public List<CharacterSequence> getWordsFromBoard(final Board board) {
        return getAllBoardLines(board).stream()
                .flatMap(this::getWords)
                .collect(Collectors.toList());
    }

    private List<CharacterSequence> getAllBoardLines(final Board board) {
        Map<Position, Optional<Character>> fieldMap = board.getCharacterMap();

        List<CharacterSequence> lines = new ArrayList<>();
        lines.addAll(getLines(
                fieldMap,
                board.getBoardParameters().getHorizontalSize(),
                board.getBoardParameters().getVerticalSize(),
                (x, y) -> Position.builder().x(x).y(y).build()));
        lines.addAll(getLines(
                fieldMap,
                board.getBoardParameters().getHorizontalSize(),
                board.getBoardParameters().getVerticalSize(),
                (x, y) -> Position.builder().x(y).y(x).build()));
        return lines;
    }

    private List<CharacterSequence> getLines(Map<Position, Optional<Character>> fieldMap,
                                  Integer horizontalSize, Integer verticalSize,
                                  BiFunction<Integer, Integer, Position> getPosition) {
        List<CharacterSequence> lines = new ArrayList<>();

        for (int x = 0; x < horizontalSize; x++) {
            CharacterSequence.CharacterSequenceBuilder lineBuilder = CharacterSequence.builder();
            for (int y = 0; y < verticalSize; y++) {
                Position position = getPosition.apply(x, y);
                Optional<Character> character = ofNullable(fieldMap.get(position))
                        .orElseThrow(() -> new IllegalStateException("There is no field on board"));

                lineBuilder.character(CharacterWithPosition.builder()
                        .character(character)
                        .x(position.getX())
                        .y(position.getY())
                        .build());
            }
            lines.add(lineBuilder.build());
        }
        return lines;
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
