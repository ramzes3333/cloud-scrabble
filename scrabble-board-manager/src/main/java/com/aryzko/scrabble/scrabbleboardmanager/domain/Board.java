package com.aryzko.scrabble.scrabbleboardmanager.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    private UUID id;
    private List<Field> fields;
    private List<Rack> racks;

    private BoardParameters boardParameters;

    public Map<Position, Character> getCharacterMap() {
        return fields.stream()
                .collect(Collectors.toMap(
                        field -> Position.builder()
                                .x(field.getX())
                                .y(field.getY())
                                .build(),
                        field -> field.getLetter().getLetter()));
    }
}
