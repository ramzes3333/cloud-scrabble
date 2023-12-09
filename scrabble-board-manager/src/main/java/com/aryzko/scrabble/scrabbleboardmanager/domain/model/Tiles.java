package com.aryzko.scrabble.scrabbleboardmanager.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang.NotImplementedException;

import java.util.List;

@Data
@AllArgsConstructor
public class Tiles {

    List<Tile> tiles;

    public Tile getTile(int x, int y) {
        return tiles.stream()
                .filter(t -> t.getX() == x && t.getY() == y)
                .findAny().orElse(null);
    }

    public Tiles transpose() {
        return new Tiles(tiles.stream()
                .map(t -> new Tile(t.getY(), t.getX(), t.getLetter(), t.isBlank()))
                .toList());
    }
}


