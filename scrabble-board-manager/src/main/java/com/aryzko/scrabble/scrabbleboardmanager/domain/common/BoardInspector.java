package com.aryzko.scrabble.scrabbleboardmanager.domain.common;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.CharacterSequence;
import com.aryzko.scrabble.scrabbleboardmanager.domain.CharacterWithPosition;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import static java.util.Optional.ofNullable;

public class BoardInspector {

    public static List<CharacterSequence> getAllBoardLines(final Board board) {
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

    private static List<CharacterSequence> getLines(final Map<Position, Optional<Character>> fieldMap,
                                             final Integer horizontalSize, final Integer verticalSize,
                                             final BiFunction<Integer, Integer, Position> getPosition) {
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
}
