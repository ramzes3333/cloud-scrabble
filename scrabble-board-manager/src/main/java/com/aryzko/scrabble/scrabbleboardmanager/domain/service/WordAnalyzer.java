package com.aryzko.scrabble.scrabbleboardmanager.domain.service;

import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Tile;
import com.aryzko.scrabble.scrabbleboardmanager.domain.model.Tiles;

import java.util.List;

public class WordAnalyzer {

    public static WordDirection determineWordDirection(Tiles tiles) {
        List<Tile> tileList = tiles.getTiles();

        if (tileList.size() == 1) {
            return WordDirection.UNDEFINED;
        } else if (isHorizontal(tileList)) {
            return WordDirection.HORIZONTAL;
        } else if (isVertical(tileList)) {
            return WordDirection.VERTICAL;
        } else {
            return WordDirection.UNDEFINED;
        }
    }

    private static boolean isHorizontal(List<Tile> tiles) {
        int y = tiles.get(0).getY();
        return tiles.stream().allMatch(tile -> tile.getY() == y);
    }

    private static boolean isVertical(List<Tile> tiles) {
        int x = tiles.get(0).getX();
        return tiles.stream().allMatch(tile -> tile.getX() == x);
    }

    public enum WordDirection {
        HORIZONTAL,
        VERTICAL,
        UNDEFINED
    }
}
