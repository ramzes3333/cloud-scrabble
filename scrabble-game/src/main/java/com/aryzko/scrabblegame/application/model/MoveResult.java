package com.aryzko.scrabblegame.application.model;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class MoveResult {
    String actualPlayerId;
    @Singular
    List<PlayerMove> playerMoves;
    GameState gameState;

    @Value
    @Builder
    public static class PlayerMove {
        String playerId;
        @Singular
        List<Tile> tiles;
        String word;
        Integer movePoints;
        Integer allPoints;
    }

    @Value
    @Builder
    public static class GameState {
        State state;
        String winnerId;
    }

    public enum State {
        NOT_STARTED,
        STARTED,
        FINISHED
    }
}
