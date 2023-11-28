package com.aryzko.scrabblegame.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Game {
    private UUID id;
    private OffsetDateTime creationDate;
    private State state;
    private UUID boardId;
    private List<Player> players = new ArrayList<>();
    private String actualPlayerId;
    private String winnerId;

    @JsonIgnore
    public Player getActualPlayer() {
        return this.getPlayers().stream()
                .filter(p -> p.getId().equals(this.getActualPlayerId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Cannot find actual player!"));
    }
}
