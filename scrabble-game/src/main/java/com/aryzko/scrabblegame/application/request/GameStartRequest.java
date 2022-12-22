package com.aryzko.scrabblegame.application.request;

import lombok.Data;

import java.util.List;

@Data
public class GameStartRequest {

    private List<BotPlayer> botPlayers;
    private List<HumanPlayer> humanPlayers;

    public record BotPlayer(Level level) {}

    public record HumanPlayer() {}

    public enum Level {
        NEWBIE,
        BEGINNER,
        ADVANCED,
        EXPERT,
        LEGEND
    }
}
