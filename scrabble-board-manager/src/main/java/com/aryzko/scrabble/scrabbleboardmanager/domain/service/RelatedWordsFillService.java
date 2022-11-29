package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.CharacterWithPosition;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Position;
import com.aryzko.scrabble.scrabbleboardmanager.domain.Solution;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;

@Service
public class RelatedWordsFillService {
    public void fill(final Board board, final Solution solution) {
        final Map<Position, Board.DirectionalField> fieldMap = board.buildFieldMap();

        solution.getWords().stream()
                .forEach(w -> fill(fieldMap, w));
    }

    private void fill(final Map<Position, Board.DirectionalField> fieldMap, Solution.Word word) {
        List<Solution.Word> relatedWords = word.getElements().stream()
                .filter(not(Solution.Word.Element::isOnBoard))
                .map(e -> prepareTemporaryField(
                        fieldMap,
                        Position.builder().x(e.getX()).y(e.getY()).build(),
                        e.getLetter()))
                .filter(this::isAdjacentToUpOrDown)
                .map(this::getWord)
                .collect(Collectors.toList());

        word.getRelatedWords().addAll(relatedWords);
    }

    private Board.DirectionalField prepareTemporaryField(final Map<Position, Board.DirectionalField> fieldMap,
                                                         Position position, Character character) {
        return Board.DirectionalField.builder()
                .field(CharacterWithPosition.builder()
                        .x(position.getX())
                        .y(position.getY())
                        .character(of(character))
                        .build())
                .up(fieldMap.get(Position.builder().x(position.getX()).y(position.getY() - 1).build()))
                .down(fieldMap.get(Position.builder().x(position.getX()).y(position.getY() + 1).build()))
                .build();
    }

    private Solution.Word getWord(Board.DirectionalField field) {
        Solution.Word.WordBuilder wordBuilder = Solution.Word.builder();
        List<Solution.Word.Element> upPart = traverse(field, Board.DirectionalField::getUp);
        Collections.reverse(upPart);
        wordBuilder.elements(upPart);
        wordBuilder.element(prepareElement(
                field,
                Character.toLowerCase(field.getField().getCharacter().get()),
                false));
        wordBuilder.elements(traverse(field, Board.DirectionalField::getDown));

        return wordBuilder.build();
    }

    private static List<Solution.Word.Element> traverse(Board.DirectionalField directionalField, Function<Board.DirectionalField, Board.DirectionalField> getNextField) {
        List<Solution.Word.Element> elements = new ArrayList<>();

        while (ofNullable(getNextField.apply(directionalField)).map(Board.DirectionalField::isCharSet).orElse(false)) {
            directionalField = getNextField.apply(directionalField);
            elements.add(prepareElement(
                    directionalField,
                    Character.toLowerCase(directionalField.getField().getCharacter().get()),
                    true));
        }
        return elements;
    }

    private static boolean isUnmodifiableLetter(int sourceX, int sourceY, Board.DirectionalField field) {
        return sourceX != field.getField().getX() || sourceY != field.getField().getY();
    }

    private static Solution.Word.Element prepareElement(Board.DirectionalField field, Character character, boolean unmodifiableLetter) {
        return Solution.Word.Element.builder()
                .x(field.getField().getX())
                .y(field.getField().getY())
                .letter(character)
                .onBoard(unmodifiableLetter)
                .build();
    }

    private boolean isAdjacentToUpOrDown(Board.DirectionalField directionalField) {
        return ofNullable(directionalField.getUp()).map(Board.DirectionalField::isCharSet).orElse(false)
                || ofNullable(directionalField.getDown()).map(Board.DirectionalField::isCharSet).orElse(false);
    }
}
