package com.aryzko.scrabblegame.domain.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class Player {
    private final Integer id;
    private final Type type;
    private Integer points;
}
