package com.aryzko.scrabblegame.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Move {
    private Integer order;
    private Integer gameOrder;
    private String word;
    private String tiles;
    private Integer points;
}
