package com.aryzko.scrabble.scrabbleboardmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
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
    private List<Field> fields;
    private List<Rack> racks;

    private BoardParameters boardParameters;

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

    public Map<Position, DirectionalField> buildFieldMap() {
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

    private static Position getPosition(Integer x, Integer y) {
        return Position.builder()
                .x(x)
                .y(y)
                .build();
    }

    @Getter
    @Setter
    @Builder
    public static class DirectionalField {
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
