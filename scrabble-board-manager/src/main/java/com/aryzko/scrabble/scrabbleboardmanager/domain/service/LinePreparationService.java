package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.CharacterWithPosition;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Position;
import com.aryzko.scrabble.scrabbleboardmanager.domain.PreparedLine;
import com.aryzko.scrabble.scrabbleboardmanager.domain.PreparedLines;
import com.aryzko.scrabble.scrabbleboardmanager.domain.provider.DictionaryProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Service
public class LinePreparationService {

    private static final int FIRST_COLUMN_X = 0;

    private final DictionaryProvider dictionaryProvider;

    protected PreparedLines prepareLines(final Board board) {

        final Map<Position, Board.DirectionalField> fieldMap = board.buildFieldMap();

        return PreparedLines.builder()
                .lines(fieldMap.values().stream()
                        .filter(field -> field.getField().getX().equals(FIRST_COLUMN_X))
                        .map(this::prepareLine)
                        .collect(Collectors.toList()))
                .build();
    }

    private PreparedLine prepareLine(Board.DirectionalField directionalField) {
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

    private void handleEmptyFieldNotAdjacentToUpOrDown(Board.DirectionalField directionalField, CharacterWithPosition character, PreparedLine.LineField.LineFieldBuilder fieldBuilder) {
        if (!character.isCharSet() && !isAdjacentToUpOrDown(directionalField)) {
            fieldBuilder.anyLetter(true);
        }
    }

    private void handleEmptyFieldAdjacentInAnyDirection(Board.DirectionalField directionalField, int leftLimit, CharacterWithPosition character, PreparedLine.LineField.LineFieldBuilder fieldBuilder) {
        if(!character.isCharSet() && isAdjacentInAnyDirections(directionalField)) {
            fieldBuilder.anchor(true);
            fieldBuilder.leftLimit(leftLimit);
        }
    }

    private void handleEmptyFieldAdjacentToUpOrDown(Board.DirectionalField directionalField, CharacterWithPosition character, PreparedLine.LineField.LineFieldBuilder fieldBuilder) {
        if (!character.isCharSet() && isAdjacentToUpOrDown(directionalField)) {
            fieldBuilder.availableLetters(computeAvailableCharacters(directionalField));
        }
    }

    private List<Character> computeAvailableCharacters(Board.DirectionalField directionalField) {
        StringBuilder verticalPattern = prepareVerticalPattern(directionalField);
        List<Character> verticalCharacters = new ArrayList<>();
        if (verticalPattern.length() > 1) {
            verticalCharacters.addAll(dictionaryProvider.fillGap(verticalPattern.toString().toLowerCase()));
        }
        return verticalCharacters;
    }

    private StringBuilder prepareVerticalPattern(Board.DirectionalField startField) {
        return traverse(startField, Board.DirectionalField::getUp)
                .reverse()
                .append("*")
                .append(traverse(startField, Board.DirectionalField::getDown));
    }

    private static StringBuilder traverse(Board.DirectionalField directionalField, Function<Board.DirectionalField, Board.DirectionalField> getNextField) {
        StringBuilder pattern = new StringBuilder();

        while (ofNullable(getNextField.apply(directionalField)).map(Board.DirectionalField::isCharSet).orElse(false)) {
            directionalField = getNextField.apply(directionalField);
            pattern.append(directionalField.getField().getCharacter().get());
        }
        return pattern;
    }

    private boolean isAdjacentToUpOrDown(Board.DirectionalField directionalField) {
        return ofNullable(directionalField.getUp()).map(Board.DirectionalField::isCharSet).orElse(false)
                || ofNullable(directionalField.getDown()).map(Board.DirectionalField::isCharSet).orElse(false);
    }

    private boolean isAdjacentInAnyDirections(Board.DirectionalField directionalField) {
        return ofNullable(directionalField.getUp()).map(Board.DirectionalField::isCharSet).orElse(false)
                || ofNullable(directionalField.getDown()).map(Board.DirectionalField::isCharSet).orElse(false)
                || ofNullable(directionalField.getLeft()).map(Board.DirectionalField::isCharSet).orElse(false)
                || ofNullable(directionalField.getRight()).map(Board.DirectionalField::isCharSet).orElse(false);
    }
}