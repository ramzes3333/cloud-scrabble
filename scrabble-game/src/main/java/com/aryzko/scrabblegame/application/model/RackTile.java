package com.aryzko.scrabblegame.application.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RackTile {
    private Character letter;
    private Integer points;
    private boolean blank;
}
