package com.aryzko.scrabble.scrabbletilemanager.application.response;

import com.aryzko.scrabble.scrabbletilemanager.domain.TileConfiguration;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class TileConfigurationResponse {
    private final List<Tile> tiles;

    public static TileConfigurationResponse of(List<TileConfiguration> tileConfigurations) {
        return new TileConfigurationResponse(tileConfigurations.stream()
                .map(tc -> new Tile(tc.getTile().getLetter(), tc.getTile().getPoints(), tc.getNumber()))
                .collect(Collectors.toList()));
    }

    public record Tile(Character letter, Integer points, Integer number) { }
}




