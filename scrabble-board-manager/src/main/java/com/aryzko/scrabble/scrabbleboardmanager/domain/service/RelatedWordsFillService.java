package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Board;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.CharacterWithPosition;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Position;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Solution;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Word;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class RelatedWordsFillService {
    public void fill(final Board board, final Solution solution) {
        final Map<Position, Board.DirectionalField> fieldMap = board.buildDirectionalFieldMap();

        solution.getWords()
                .forEach(w -> w.getRelatedWords().addAll(getRelatedWords(fieldMap, w)));
    }

    public List<Word> getRelatedWords(final Board board, final Word word) {
        final Map<Position, Board.DirectionalField> fieldMap = board.buildDirectionalFieldMap();
        List<Word> relatedWords = getRelatedWords(fieldMap, word);
        log.info("Found related words to %s: %s".formatted(word.getWordAsString(), relatedWords.stream()
                .map(Word::getWordAsString)
                .collect(Collectors.joining(", "))));
        return relatedWords;
    }

    private List<Word> getRelatedWords(final Map<Position, Board.DirectionalField> fieldMap, Word word) {
        return word.getElements().stream()
                .filter(not(Word.Element::isOnBoard))
                .map(e -> prepareTemporaryField(
                        fieldMap,
                        Position.builder().x(e.getX()).y(e.getY()).build(),
                        e.getLetter(),
                        e.isBlank()))
                .filter(this::isAdjacentToUpOrDown)
                .map(this::getWord)
                .toList();
    }

    private PotentialField prepareTemporaryField(final Map<Position, Board.DirectionalField> fieldMap,
                                                 Position position, Character character,
                                                 boolean isBlank) {
        return PotentialField.builder()
                .field(CharacterWithPosition.builder()
                        .x(position.getX())
                        .y(position.getY())
                        .character(of(character))
                        .build())
                .up(fieldMap.get(Position.builder().x(position.getX()).y(position.getY() - 1).build()))
                .down(fieldMap.get(Position.builder().x(position.getX()).y(position.getY() + 1).build()))
                .blank(isBlank)
                .build();
    }

    private Word getWord(PotentialField field) {
        Word.WordBuilder wordBuilder = Word.builder();
        List<Word.Element> upPart = traverse(field, Board.DirectionalField::getUp);
        Collections.reverse(upPart);
        wordBuilder.elements(upPart);
        wordBuilder.element(prepareElement(
                field,
                Character.toLowerCase(field.getField().getCharacter().get()),
                field.isBlank(),
                false));
        wordBuilder.elements(traverse(field, Board.DirectionalField::getDown));

        return wordBuilder.build();
    }

    private static List<Word.Element> traverse(Board.DirectionalField directionalField, Function<Board.DirectionalField, Board.DirectionalField> getNextField) {
        List<Word.Element> elements = new ArrayList<>();

        while (ofNullable(getNextField.apply(directionalField)).map(Board.DirectionalField::isCharSet).orElse(false)) {
            directionalField = getNextField.apply(directionalField);
            elements.add(prepareElement(
                    directionalField,
                    Character.toLowerCase(directionalField.getField().getCharacter().get()),
                    false,
                    true));
        }
        return elements;
    }

    private static Word.Element prepareElement(Board.DirectionalField field,
                                               Character character,
                                               boolean blank,
                                               boolean unmodifiableLetter) {
        return Word.Element.builder()
                .x(field.getField().getX())
                .y(field.getField().getY())
                .letter(character)
                .blank(blank)
                .onBoard(unmodifiableLetter)
                .build();
    }

    private boolean isAdjacentToUpOrDown(Board.DirectionalField directionalField) {
        return ofNullable(directionalField.getUp()).map(Board.DirectionalField::isCharSet).orElse(false)
                || ofNullable(directionalField.getDown()).map(Board.DirectionalField::isCharSet).orElse(false);
    }

    @Getter
    @SuperBuilder
    private static class PotentialField extends Board.DirectionalField {
        private boolean blank;
    }
}
