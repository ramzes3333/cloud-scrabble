package com.aryzko.scrabblegame.application.response;

import com.aryzko.scrabblegame.application.model.Player;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class GameStartResponse {
    private Long id;
    private String boardId;
    private List<Player> players;

    private String actualPlayerId;
    private String winnerId;
}
