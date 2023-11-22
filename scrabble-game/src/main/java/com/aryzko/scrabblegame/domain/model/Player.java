package com.aryzko.scrabblegame.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
public abstract class Player {
    private final String id;
    @Builder.Default
    private Integer points = 0;
    private Integer order;
    @Builder.Default
    private List<Move> moveHistory = new ArrayList<>();

    public abstract Type getType();
}
