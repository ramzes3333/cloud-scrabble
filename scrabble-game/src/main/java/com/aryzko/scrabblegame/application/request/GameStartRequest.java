package com.aryzko.scrabblegame.application.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class GameStartRequest {

    @Valid
    private List<BotPlayer> botPlayers;
    @Valid
    private List<HumanPlayer> humanPlayers;

    public record BotPlayer(@NotNull Level level) {}

    public record HumanPlayer() {}

    public enum Level {
        NEWBIE,
        BEGINNER,
        ADVANCED,
        EXPERT,
        LEGEND
    }
}
