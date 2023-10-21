package com.aryzko.scrabblegame.domain.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class Player {
    private final String id;
    private Integer points;
    private Integer order;

    public abstract Type getType();
}
