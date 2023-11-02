package com.aryzko.scrabblegame.application.request;

import com.aryzko.scrabblegame.application.model.Tile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class GameMoveRequest {
    @NotNull
    private String gameId;
    @NotBlank
    private String playerId;
    private List<Tile> tiles;
}
