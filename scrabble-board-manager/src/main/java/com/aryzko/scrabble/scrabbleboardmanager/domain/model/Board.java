package com.aryzko.scrabble.scrabbleboardmanager.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang.NotImplementedException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Board {
    @Id
    private UUID id;
    private OffsetDateTime creationDate;
    private List<Field> fields;
    private List<Rack> racks;
    private BoardParameters boardParameters;

    @JsonIgnore
    public boolean isEmpty() {
        return fields.stream().noneMatch(f -> f.getLetter() != null);
    }

    @JsonIgnore
    public Map<Position, Optional<Character>> getCharacterMap() {
        return fields.stream()
                .collect(Collectors.toMap(
                        field -> Position.builder()
                                .x(field.getX())
                                .y(field.getY())
                                .build(),
                        field -> ofNullable(field.getLetter())
                                .map(Letter::getLetter)));
    }

    @JsonIgnore
    public Map<Position, Field> geFieldMap() {
        return fields.stream()
                .collect(Collectors.toMap(
                        field -> Position.builder()
                                .x(field.getX())
                                .y(field.getY())
                                .build(),
                        field -> field));
    }

    @JsonIgnore
    public Map<Position, Bonus> getBonusMap() {
        return fields.stream()
                .collect(Collectors.toMap(
                        field -> Position.builder()
                                .x(field.getX())
                                .y(field.getY())
                                .build(),
                        field -> ofNullable(field.getBonus()).
                                orElse(Bonus.None)));
    }

    public Map<Position, DirectionalField> buildDirectionalFieldMap() {
        Map<Position, Optional<Character>> characterMap = this.getCharacterMap();

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

    public List<Position> getPositionsWithBlank() {
        return fields.stream()
                .filter(field -> field.getLetter() != null && field.getLetter().isBlank())
                .map(field -> getPosition(field.getX(), field.getY()))
                .collect(Collectors.toList());
    }

    private static Position getPosition(Integer x, Integer y) {
        return Position.builder()
                .x(x)
                .y(y)
                .build();
    }

    public Board transpose(TransposeType transposeType) {
        Board transposed = new Board();
        transposed.setId(UUID.fromString(this.getId().toString()));
        transposed.setBoardParameters(BoardParameters.builder()
                .horizontalSize(boardParameters.getHorizontalSize())
                .verticalSize(boardParameters.getVerticalSize())
                .rackSize(boardParameters.getRackSize())
                .build());
        transposed.setRacks(racks.stream()
                .map(Rack::clone)
                .collect(Collectors.toList()));
        transposed.setFields(fields.stream()
                .map(field -> transpose(field, transposeType))
                .collect(Collectors.toList()));
        return transposed;
    }

    private Field transpose(Field field, TransposeType transposeType) {
        if(transposeType == TransposeType.FLIP_HORIZONTALLY_AND_ROTATE_RIGHT) {
            throw new NotImplementedException();
        }
        Field transposedField = new Field();
        transposedField.setX(field.getY());
        transposedField.setY(field.getX());
        transposedField.setBonus(field.getBonus());
        transposedField.setLetter(field.getLetter());
        return transposedField;
    }

    @Getter
    @SuperBuilder
    public static class DirectionalField {
        private final CharacterWithPosition field;

        @Setter
        private DirectionalField up;
        @Setter
        private DirectionalField down;
        @Setter
        private DirectionalField left;
        @Setter
        private DirectionalField right;

        public boolean isCharSet() {
            return field.getCharacter().isPresent();
        }
    }
}
