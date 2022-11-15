package com.aryzko.scrabble.scrabbleboardmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
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
}
