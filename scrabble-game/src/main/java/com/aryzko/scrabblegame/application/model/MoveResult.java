package com.aryzko.scrabblegame.application.model;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MoveResult {
    @Singular
    private List<PlayerMove> playerMoves;

    @Value
    @Builder
    public static class PlayerMove {
        private String playerId;
        @Singular
        private List<Tile> moveTiles;
        private Integer movePoints;
        private Integer allPoints;
    }
}