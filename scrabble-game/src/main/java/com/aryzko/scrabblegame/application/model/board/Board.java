package com.aryzko.scrabblegame.application.model.board;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.function.Predicate;

@Data
@AllArgsConstructor
public class Board {
    private String id;
    private OffsetDateTime creationDate;
    private List<Field> fields;
    private List<Rack> racks;
    private BoardParameters boardParameters;

    public Field getField(Integer x, Integer y) {
        return fields.stream()
                .filter(fieldCoordinatesPredicate(x, y))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No field with x: %d and y: %d".formatted(x, y)));
    }

    private static Predicate<Field> fieldCoordinatesPredicate(Integer x, Integer y) {
        return f -> f.getX().equals(x) && f.getY().equals(y);
    }
}
