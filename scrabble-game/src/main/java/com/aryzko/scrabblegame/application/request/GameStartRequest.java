package com.aryzko.scrabblegame.application.request;

import lombok.Data;

import java.util.List;

@Data
public class GameStartRequest {

    private List<BotPlayer> botPlayers;

    public record BotPlayer(Level level) {}

    public enum Level {
        NEWBIE,
        BEGINNER,
        ADVANCED,
        EXPERT,
        LEGEND
    }
}
