package com.aryzko.scrabblegame.application.model.board;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Letter {
    private Character letter;
    private Integer points;
    private boolean blank;

    public boolean isOccupied() {
        return letter != null || blank;
    }
}
