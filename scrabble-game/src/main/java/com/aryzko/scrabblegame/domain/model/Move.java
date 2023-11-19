package com.aryzko.scrabblegame.domain.model;

import lombok.Data;

@Data
public class Move {
    private Integer order;
    private String word;
    private Integer points;
}
