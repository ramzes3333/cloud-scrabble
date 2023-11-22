package com.aryzko.scrabble.scrabbleboardmanager.domain.provider.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class TileConfiguration {
    private final List<Tile> tiles;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TileConfiguration(@JsonProperty("tiles") List<Tile> tiles) {
        this.tiles = tiles;
    }

    public record Tile(Character letter, Integer points, Integer number) { }


    public Map<Character, Integer> getPointsMap() {
        return tiles.stream().collect(Collectors.toMap(
                Tile::letter,
                Tile::points
        ));
    }
}
