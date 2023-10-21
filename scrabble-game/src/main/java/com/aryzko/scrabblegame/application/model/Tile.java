package com.aryzko.scrabblegame.application.model;

import lombok.Value;

@Value
public class Tile {
    private Integer x;
    private Integer y;
    private Character letter;
    private Integer points;
    private boolean blank;
}
