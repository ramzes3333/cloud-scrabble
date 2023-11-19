package com.aryzko.scrabblegame.domain.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
public abstract class Player {
    private final String id;
    private Integer points = 0;
    private Integer order;
    private List<Move> moveHistory;

    public abstract Type getType();
}
