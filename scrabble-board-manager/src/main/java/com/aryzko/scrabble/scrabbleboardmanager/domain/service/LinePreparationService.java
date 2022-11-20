package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.CharacterWithPosition;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Position;
import com.aryzko.scrabble.scrabbleboardmanager.domain.PreparedLine;
import com.aryzko.scrabble.scrabbleboardmanager.domain.PreparedLines;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.DictionaryProvider;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Service
public class LinePreparationService {

    private static final int FIRST_COLUMN_X = 0;

    private final DictionaryProvider dictionaryProvider;

    private static StringBuilder traverse(DirectionalField directionalField, Function<DirectionalField, DirectionalField> getNextField) {
        StringBuilder pattern = new StringBuilder();

        while (ofNullable(getNextField.apply(directionalField)).map(DirectionalField::isCharSet).orElse(false)) {
            directionalField = getNextField.apply(directionalField);
            pattern.append(directionalField.getField().getCharacter().get());
        }
        return pattern;
    }

    private static List<Character> concat(List<Character> list1, List<Character> list2) {
        return Stream.concat(list1.stream(), list2.stream())
                .map(Character::toLowerCase)
                .collect(Collectors.toList());
    }

    private static List<Character> intersection(List<Character> list1, List<Character> list2) {
        return list1.stream()
                .distinct()
                .filter(list2::contains)
                .map(Character::toLowerCase)
                .collect(Collectors.toList());
    }

    private static Position getPosition(Integer x, Integer y) {
        return Position.builder()
                .x(x)
                .y(y)
                .build();
    }

    protected PreparedLines prepareLines(final Board board) {

        final Map<Position, DirectionalField> fieldMap = buildFieldMap(board);

        return PreparedLines.builder()
                .lines(fieldMap.values().stream()
                        .filter(field -> field.getField().getX().equals(FIRST_COLUMN_X))
                        .map(this::prepareLine)
                        .collect(Collectors.toList()))
                .build();
    }

    private PreparedLine prepareLine(DirectionalField directionalField) {
        PreparedLine.PreparedLineBuilder preparedLineBuilder = PreparedLine.builder();
        int leftLimit = 0;
        do {
            CharacterWithPosition character = directionalField.getField();
            boolean isAdjacent = isAdjacent(directionalField);

            PreparedLine.LineField.LineFieldBuilder fieldBuilder = PreparedLine.LineField.builder().x(character.getX()).y(character.getY());

            if (!character.isCharSet() && isAdjacent) {
                fieldBuilder.availableLetters(computeAvailableCharacters(directionalField));
                fieldBuilder.anchor(true);
                fieldBuilder.leftLimit(leftLimit);
                leftLimit = 0;
            }
            if (!character.isCharSet() && !isAdjacent) {
                fieldBuilder.anyLetter(true);
                leftLimit++;
            }
            if (character.isCharSet()) {
                fieldBuilder.letter(Character.toLowerCase(character.getCharacter().get()));
                leftLimit = 0;
            }
            preparedLineBuilder.field(fieldBuilder.build());

            directionalField = directionalField.getRight();
        } while (directionalField != null);
        return preparedLineBuilder.build();
    }

    private List<Character> computeAvailableCharacters(DirectionalField directionalField) {
        StringBuilder verticalPattern = prepareVerticalPattern(directionalField);
        StringBuilder horizontalPattern = prepareHorizontalPattern(directionalField);

        List<Character> verticalCharacters = new ArrayList<>();
        List<Character> horizontalCharacters = new ArrayList<>();
        if (verticalPattern.length() > 1) {
            verticalCharacters.addAll(dictionaryProvider.fillGap(verticalPattern.toString().toLowerCase()));
        }
        if (horizontalPattern.length() > 1) {
            horizontalCharacters.addAll(dictionaryProvider.fillGap(horizontalPattern.toString().toLowerCase()));
        }

        if (!horizontalCharacters.isEmpty() && !verticalCharacters.isEmpty()) {
            return intersection(verticalCharacters, horizontalCharacters);
        } else {
            return concat(verticalCharacters, horizontalCharacters);
        }
    }

    private StringBuilder prepareHorizontalPattern(DirectionalField startField) {
        return traverse(startField, DirectionalField::getLeft)
                .reverse()
                .append("*")
                .append(traverse(startField, DirectionalField::getRight));
    }

    private StringBuilder prepareVerticalPattern(DirectionalField startField) {
        return traverse(startField, DirectionalField::getUp)
                .reverse()
                .append("*")
                .append(traverse(startField, DirectionalField::getDown));
    }

    private boolean isAdjacent(DirectionalField directionalField) {
        return ofNullable(directionalField.getUp()).map(DirectionalField::isCharSet).orElse(false)
                || ofNullable(directionalField.getDown()).map(DirectionalField::isCharSet).orElse(false)
                || ofNullable(directionalField.getLeft()).map(DirectionalField::isCharSet).orElse(false)
                || ofNullable(directionalField.getRight()).map(DirectionalField::isCharSet).orElse(false);
    }

    private Map<Position, DirectionalField> buildFieldMap(final Board board) {
        Map<Position, Optional<Character>> characterMap = board.getCharacterMap();

        Map<Position, DirectionalField> fieldMap = characterMap.keySet().stream()
                .map(character ->
                        DirectionalField.builder().field(
                                        CharacterWithPosition.builder()
                                                .x(character.getX())
                                                .y(character.getY())
                                                .character(characterMap.get(getPosition(character.getX(), character.getY())))
                                                .build())
                                .build())
                .collect(Collectors.toMap(
                        directionalField -> getPosition(directionalField.getField().getX(), directionalField.getField().getY()),
                        directionalField -> directionalField));

        fieldMap.values()
                .forEach(field -> {
                    field.setUp(fieldMap.get(getPosition(field.getField().getX(), field.getField().getY() - 1)));
                    field.setDown(fieldMap.get(getPosition(field.getField().getX(), field.getField().getY() + 1)));
                    field.setLeft(fieldMap.get(getPosition(field.getField().getX() - 1, field.getField().getY())));
                    field.setRight(fieldMap.get(getPosition(field.getField().getX() + 1, field.getField().getY())));
                });

        return fieldMap;
    }

    @Getter
    @Setter
    @Builder
    private static class DirectionalField {
        private final CharacterWithPosition field;

        private DirectionalField up;
        private DirectionalField down;
        private DirectionalField left;
        private DirectionalField right;

        public boolean isCharSet() {
            return field.getCharacter().isPresent();
        }
    }
}