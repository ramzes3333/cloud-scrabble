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

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Service
public class LinePreparationService {

    private static final int FIRST_COLUMN_X = 0;

    private final DictionaryProvider dictionaryProvider;

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
            PreparedLine.LineField.LineFieldBuilder fieldBuilder = PreparedLine.LineField.builder().x(character.getX()).y(character.getY());

            handleEmptyFieldAdjacentToUpOrDown(directionalField, character, fieldBuilder);
            handleEmptyFieldAdjacentInAnyDirection(directionalField, leftLimit, character, fieldBuilder);
            handleEmptyFieldNotAdjacentToUpOrDown(directionalField, character, fieldBuilder);
            handleFieldWithChar(character, fieldBuilder);

            if(character.isCharSet() || isAdjacentInAnyDirections(directionalField)) {
                leftLimit = 0;
            } else {
                leftLimit++;
            }
            preparedLineBuilder.field(fieldBuilder.build());

            directionalField = directionalField.getRight();
        } while (directionalField != null);
        return preparedLineBuilder.build();
    }

    private void handleFieldWithChar(CharacterWithPosition character, PreparedLine.LineField.LineFieldBuilder fieldBuilder) {
        if (character.isCharSet()) {
            fieldBuilder.letter(Character.toLowerCase(character.getCharacter().get()));
        }
    }

    private void handleEmptyFieldNotAdjacentToUpOrDown(DirectionalField directionalField, CharacterWithPosition character, PreparedLine.LineField.LineFieldBuilder fieldBuilder) {
        if (!character.isCharSet() && !isAdjacentToUpOrDown(directionalField)) {
            fieldBuilder.anyLetter(true);
        }
    }

    private void handleEmptyFieldAdjacentInAnyDirection(DirectionalField directionalField, int leftLimit, CharacterWithPosition character, PreparedLine.LineField.LineFieldBuilder fieldBuilder) {
        if(!character.isCharSet() && isAdjacentInAnyDirections(directionalField)) {
            fieldBuilder.anchor(true);
            fieldBuilder.leftLimit(leftLimit);
        }
    }

    private void handleEmptyFieldAdjacentToUpOrDown(DirectionalField directionalField, CharacterWithPosition character, PreparedLine.LineField.LineFieldBuilder fieldBuilder) {
        if (!character.isCharSet() && isAdjacentToUpOrDown(directionalField)) {
            fieldBuilder.availableLetters(computeAvailableCharacters(directionalField));
        }
    }

    private List<Character> computeAvailableCharacters(DirectionalField directionalField) {
        StringBuilder verticalPattern = prepareVerticalPattern(directionalField);
        List<Character> verticalCharacters = new ArrayList<>();
        if (verticalPattern.length() > 1) {
            verticalCharacters.addAll(dictionaryProvider.fillGap(verticalPattern.toString().toLowerCase()));
        }
        return verticalCharacters;
    }

    private StringBuilder prepareVerticalPattern(DirectionalField startField) {
        return traverse(startField, DirectionalField::getUp)
                .reverse()
                .append("*")
                .append(traverse(startField, DirectionalField::getDown));
    }

    private static StringBuilder traverse(DirectionalField directionalField, Function<DirectionalField, DirectionalField> getNextField) {
        StringBuilder pattern = new StringBuilder();

        while (ofNullable(getNextField.apply(directionalField)).map(DirectionalField::isCharSet).orElse(false)) {
            directionalField = getNextField.apply(directionalField);
            pattern.append(directionalField.getField().getCharacter().get());
        }
        return pattern;
    }

    private static Position getPosition(Integer x, Integer y) {
        return Position.builder()
                .x(x)
                .y(y)
                .build();
    }

    private boolean isAdjacentToUpOrDown(DirectionalField directionalField) {
        return ofNullable(directionalField.getUp()).map(DirectionalField::isCharSet).orElse(false)
                || ofNullable(directionalField.getDown()).map(DirectionalField::isCharSet).orElse(false);
    }

    private boolean isAdjacentInAnyDirections(DirectionalField directionalField) {
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