package com.aryzko.scrabble.scrabbletilemanager.domain.service;

import com.aryzko.scrabble.scrabbletilemanager.domain.BoardTiles;
import com.aryzko.scrabble.scrabbletilemanager.domain.Tile;
import com.aryzko.scrabble.scrabbletilemanager.domain.TileConfiguration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Optional.ofNullable;

@Service
public class RandomTileService {
    public List<Tile> getRandomTiles(BoardTiles boardTiles, Integer numberOfItems) {
        List<Character> usedTiles = getUsedTiles(boardTiles);
        List<TileConfiguration> tilesFromConfig = boardTiles.getTileSet().getTileConfigurations();
        List<TileConfiguration> possibleTiles = getPossibleTiles(tilesFromConfig, usedTiles);
        return getRandomTiles(possibleTiles, numberOfItems);
    }

    private static List<Tile> getRandomTiles(List<TileConfiguration> possibleTiles, Integer numberOfItems) {
        List<Tile> tiles = possibleTiles.stream()
                .<Tile>mapMulti(((tileConfiguration, consumer) -> {
                    IntStream.range(0, tileConfiguration.getNumber())
                            .forEach(index -> consumer.accept(tileConfiguration.getTile()));
                }))
                .collect(Collectors.toList());

        Collections.shuffle(tiles);
        return tiles.stream().limit(numberOfItems).collect(Collectors.toList());
    }

    private static List<Character> getUsedTiles(BoardTiles boardTiles) {
        return ofNullable(boardTiles.getTiles()).orElse(new ArrayList<>()).stream()
                .map(Tile::getLetter)
                .collect(Collectors.toList());
    }

    private static List<TileConfiguration> getPossibleTiles(List<TileConfiguration> tilesFromConfig, List<Character> usedTiles) {
        return tilesFromConfig.stream()
                .map(tc -> TileConfiguration.builder()
                        .tile(tc.getTile())
                        .number(tc.getNumber() - countUsedTile(usedTiles, tc.getTile().getLetter()))
                        .build())
                .collect(Collectors.toList());
    }

    private static int countUsedTile(List<Character> usedTiles, Character letter) {
        return (int) usedTiles.stream()
                .filter(ch -> ch.equals(letter))
                .count();
    }
}
